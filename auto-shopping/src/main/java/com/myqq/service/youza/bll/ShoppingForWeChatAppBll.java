package com.myqq.service.youza.bll;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.util.FileOperation;
import com.myqq.service.youza.constinfo.ConstInfoForApp;
import com.myqq.service.youza.entity.ShoppingForAppDTO;
import com.myqq.service.youza.entity.ShoppingForWeChatAppDTO;
import com.myqq.service.youza.entity.ToBuyGoodInfoAppDTO;
import com.myqq.service.youza.util.CustomThreadFactory;
import com.myqq.service.youza.util.TimeUtil;
import org.apache.http.entity.StringEntity;
import org.omg.CORBA.BooleanHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.myqq.service.youza.bll.RequestBllForApp.doPostForShoppingCar;
import static com.myqq.service.youza.constinfo.ConstInfoForApp.stockInfoFileName;
import static com.myqq.service.youza.constinfo.ConstInfoForWeChatApp.*;
import static com.myqq.service.youza.constinfo.ConstInfoForWeChatApp.zzOpenId;

public class ShoppingForWeChatAppBll {
    static final List<ShoppingForAppDTO.GoodDataStockDetailDTO> preToBuyGoodList = new ArrayList<>(50);
    /**
     * 线程池
     */
    public static ThreadPoolExecutor executorServiceForGoodDetail = new ThreadPoolExecutor (5, 20, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100), new CustomThreadFactory("GoodDetail")) {
    };
    public static ThreadPoolExecutor executorServiceForCommitOrder = new ThreadPoolExecutor (6, 20, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100), new CustomThreadFactory("CommitOrder")) {
    };

    public static ThreadPoolExecutor executorServiceForCancelOrder = new ThreadPoolExecutor (3, 10, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100), new CustomThreadFactory("CancelOrder")) {
    };

    public static List<String> quantifiers = new ArrayList<>(Arrays.asList("匹","张","尾","条","个","网","棵","只","支","颗","群","砣","座","贯","扎","捆","刀","打","手","队","单","双","对","口","头","脚","枝","件","贴","名","位","本","页","丝","两","斤","升","盘","碗","碟","叠","笼","盆","盒","杯","钟","锅","簋","篮","盘","桶","罐","瓶","壶","盏","箩","箱","袋","分","夜","代","丸","泡","粒","颗","幢","堆","包","提"));
    /**
     * 商品mainId，优惠信息编号
     */
    public static Map<String,List<String>> mainGoodIdPromotionIdsMap = new HashMap<>(100);

    /**
     * 匹配意向单信息
     *
     * @param toBuyGoodAndAddressInfos
     * @param auth
     */
    public static void buildToBuyGoodInfo(List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> toBuyGoodAndAddressInfos, String auth) {
        StringBuilder operationInfo = new StringBuilder("");
        if(toBuyGoodAndAddressInfos == null || toBuyGoodAndAddressInfos.isEmpty()){
            return;
        }
        /**
         * 每日上新
         * new goods every day
         */
        String newGoodsInfo = RequestBllForApp.doGet(getNewGoodListUrlForApp,auth);
        //System.out.println(TimeUtil.getCurrentTimeString() +" newGoodsInfo:"+newGoodsInfo);
        ShoppingForWeChatAppDTO.GoodsListDTO newGoodsList = null;
        BooleanHolder goodsInNew = new BooleanHolder(true);
        try {
            newGoodsList = JSONObject.parseObject(newGoodsInfo, ShoppingForWeChatAppDTO.GoodsListDTO.class);
            if(CollectionUtils.isEmpty(newGoodsList.getData().getRows()) && !CollectionUtils.isEmpty(newGoodsList.getData().getToday())){
                newGoodsList.getData().setRows(newGoodsList.getData().getToday());
            }
            BooleanHolder finalGoodsInNew = goodsInNew;
            ShoppingForWeChatAppDTO.GoodsListDTO finalNewGoodsList1 = newGoodsList;
            toBuyGoodAndAddressInfos.stream().forEach(item -> {
                if(finalNewGoodsList1.getData().getRows().stream().noneMatch(good -> good.getName().toLowerCase().contains(item.getShotGoodName().toLowerCase()) && (StringUtils.isEmpty(item.getShotGoodNameExtra()) || good.getName().toLowerCase().contains(item.getShotGoodNameExtra().trim().toLowerCase())))){
                    finalGoodsInNew.value = false;
                }
            });
        }catch (Exception ex){
            goodsInNew.value = false;
            System.out.println(TimeUtil.getCurrentTimeString() + ex.getMessage());
        }
        /**
         * 商城
         * goods in mall
         */
        String goodsInfo = null;
        ShoppingForWeChatAppDTO.GoodsListDTO goodsList = new ShoppingForWeChatAppDTO.GoodsListDTO();
        if(goodsInNew.value){
            goodsList.setData(new ShoppingForWeChatAppDTO.GoodsListDataDTO());
            goodsList.getData().setRows(new ArrayList<>());
        }else {
            try {
                goodsInfo = RequestBllForApp.doGet(getGoodListUrlForApp, auth);
                goodsList = JSONObject.parseObject(goodsInfo, ShoppingForWeChatAppDTO.GoodsListDTO.class);
            }catch (Exception ex){
                System.out.println(TimeUtil.getCurrentTimeString() + ex.getMessage());
            }
        }
        System.out.println(TimeUtil.getCurrentTimeString() + " newGoodsInfo contains all goods: "+goodsInNew.value);
        //System.out.println(TimeUtil.getCurrentTimeString() +" goodsInfo:"+goodsInfo);
        List<Future<Boolean>> taskList = new ArrayList<>();
        ShoppingForWeChatAppDTO.GoodsListDTO finalGoodsList1 = goodsList;
        ShoppingForWeChatAppDTO.GoodsListDTO finalNewGoodsList = newGoodsList;
        toBuyGoodAndAddressInfos.forEach(goodInfo -> {
            if(goodInfo != null && goodInfo.getAddressDetailInfo() != null ){
                String shotShopName = goodInfo.getShotGoodName();
                String shotShopNameExtra = goodInfo.getShotGoodNameExtra();
                List<String> skuColorKeyWords = goodInfo.getGoodColorKeyWords();
                List<String> skuSizeKeyWords = goodInfo.getGoodSizeKeyWords();
                List<String> skuStyleKeyWords = goodInfo.getGoodStyleKeyWords();
                List<String> skuSpecKeyWords = goodInfo.getGoodSpecKeyWords();

                List<ShoppingForAppDTO.GoodDataStockDetailDTO> realToBuyGoodList = new ArrayList<>(10);
                /*if(goodInfo.getToBuyGoodInfoList() == null){
                    goodInfo.setToBuyGoodInfoList(realToBuyGoodList);
                }*/
                goodInfo.setToBuyGoodInfoList(realToBuyGoodList);

                if(finalGoodsList1 != null && finalGoodsList1.getData() != null && finalGoodsList1.getData().getRows() != null){
                    if(finalNewGoodsList != null && finalNewGoodsList.getData() != null && finalNewGoodsList.getData().getRows() != null){
                        ShoppingForWeChatAppDTO.GoodsListDTO finalGoodsList = finalGoodsList1;
                        finalNewGoodsList.getData().getRows().removeIf(newGood -> finalGoodsList.getData().getRows().stream().anyMatch(existGood -> existGood.getGoodsId().equalsIgnoreCase(newGood.getGoodsId())));
                        if(!finalNewGoodsList.getData().getRows().isEmpty()){
                            finalGoodsList1.getData().getRows().addAll(finalNewGoodsList.getData().getRows());
                        }
                    }
                    System.out.println(AutoShoppingEntryForApp.dateTimeFormatter.format(LocalDateTime.now()) +" combined goodsList :" + finalGoodsList1.getData().getRows().size());
                    finalGoodsList1.getData().getRows().stream().filter(item -> item.getInventoryAmount() >= 0).forEach(item -> {
                        if(shotShopName != null && !shotShopName.isEmpty()){
                            boolean nameMatch = item.getName().toLowerCase().contains(shotShopName.toLowerCase()) && (goodInfo.getToBuySellType() == 1 ? (item.getName().contains("现货") || !item.getName().contains("发货")) : true);
                            if(!StringUtils.isEmpty(shotShopNameExtra)){
                                nameMatch = nameMatch && item.getName().toLowerCase().contains(shotShopNameExtra.toLowerCase());
                            }
                            if(goodInfo.getQuantifierNum() != null && !goodInfo.getQuantifierNum().isEmpty()){
                                nameMatch = nameMatch && quantifiers.stream().anyMatch(qItem -> item.getName().contains(goodInfo.getQuantifierNum()+qItem));
                            }
                            if(nameMatch){
                                System.out.println(TimeUtil.getCurrentTimeString() +" shotShopName:" + shotShopName + " "+ item.toString());
                                ShoppingForAppDTO.GoodDataStockDetailDTO toBuyGoodInfo = realToBuyGoodList.stream().filter(good -> good.getMainGoodsId().equalsIgnoreCase(item.getGoodsId())).findFirst().orElse(null);
                                if(toBuyGoodInfo == null){
                                    toBuyGoodInfo = new ShoppingForAppDTO.GoodDataStockDetailDTO();
                                    //realToBuyGoodList.add(toBuyGoodInfo);
                                    toBuyGoodInfo.setMainGoodsId(item.getGoodsId());
                                    toBuyGoodInfo.setName(item.getName());

                                    ShoppingForAppDTO.GoodDataStockDetailDTO finalToBuyGoodInfo1 = toBuyGoodInfo;
                                    System.out.println(TimeUtil.getCurrentTimeString() + " getToBuyGoodSkuInfoDetail buy new :"+JSONObject.toJSONString(goodInfo));
                                    taskList.add(executorServiceForGoodDetail.submit(() -> {getToBuyGoodSkuInfoDetail(item, finalToBuyGoodInfo1, skuColorKeyWords,skuSizeKeyWords, skuStyleKeyWords, skuSpecKeyWords, goodInfo.getToBuyNum(), auth, realToBuyGoodList); return Boolean.TRUE; }));
                                }else {
                                    ShoppingForAppDTO.GoodDataStockDetailDTO finalToBuyGoodInfo = toBuyGoodInfo;
                                    System.out.println(TimeUtil.getCurrentTimeString() + " getToBuyGoodSkuInfoDetail update:"+JSONObject.toJSONString(goodInfo));
                                    taskList.add(executorServiceForGoodDetail.submit(() -> {getToBuyGoodSkuInfoDetail(item, finalToBuyGoodInfo, skuColorKeyWords,skuSizeKeyWords, skuStyleKeyWords, skuSpecKeyWords, goodInfo.getToBuyNum(), auth, realToBuyGoodList); return true;}));
                                }
                            }
                        }
                    });
                }
            }
        });

        taskList.forEach(task -> {
            try {
                task.get();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                operationInfo.append(ex.getMessage());
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                operationInfo.append(ex.getMessage());
            }
        });

        if(!operationInfo.toString().isEmpty()){
            System.out.println(TimeUtil.getCurrentTimeString() + "buildToBuyGoodInfo operationInfo: "+ operationInfo.toString());
        }
    }

    /**
     * 构建商品明细
     *  @param goodItem
     * @param finalToBuyGoodInfo
     * @param skuColorKeyWords
     * @param skuSizeKeyWords
     * @param skuStyleKeyWords
     * @param skuSpecKeyWords
     * @param toBuyNum
     * @param auth
     * @param realToBuyGoodList
     */
    private static void getToBuyGoodSkuInfoDetail(ShoppingForWeChatAppDTO.GoodsListDataRowDTO goodItem, ShoppingForAppDTO.GoodDataStockDetailDTO finalToBuyGoodInfo, List<String> skuColorKeyWords, List<String> skuSizeKeyWords, List<String> skuStyleKeyWords, List<String> skuSpecKeyWords, int toBuyNum, String auth, List<ShoppingForAppDTO.GoodDataStockDetailDTO> realToBuyGoodList) {
        StringBuilder operationInfo = new StringBuilder("");
        try {
            String goodDataInfo = RequestBllForApp.doGet(MessageFormat.format(getGoodDataDetailUrlForApp, goodItem.getGoodsId()), auth);
            if(goodDataInfo == null || goodDataInfo.isEmpty()){
                return;
            }
            boolean hasStockData = false;
            ShoppingForAppDTO.GoodDataDTO goodData = JSONObject.parseObject(goodDataInfo, ShoppingForAppDTO.GoodDataDTO.class);
            if(goodData != null && goodData.getData().getGoodsList() != null && !goodData.getData().getGoodsList().isEmpty()){
                ShoppingForAppDTO.GoodDataDetailDTO goodDataDetail = goodData.getData();
                updateMainGoodIdAndPromotionInfo(goodItem.getGoodsId(), goodData.getData());
                //无属性信息时，忽略设置的属性信息
                boolean hasAttrInfo = goodDataDetail.getSpecList() != null && !goodDataDetail.getSpecList().isEmpty();
                resetGoodDetailSpecListInfo(goodDataDetail);
                goodDataDetail.getGoodsList().forEach(item -> {
                    if(item.getInventory() == null){
                        item.setInventory(goodItem.getInventoryAmount());
                    }
                });
                goodDataDetail.getGoodsList().stream().filter(item -> Optional.ofNullable(item.getInventory()).orElse(0) > 0).forEach(goodDetail -> {
                    boolean isMatchSku = true;
                    if(hasAttrInfo){
                        if(goodDetail.getSpecList().stream().anyMatch(propItem -> propItem.getK().contains("颜色") || propItem.getK().contains("规格")) && skuColorKeyWords != null && !skuColorKeyWords.isEmpty()){
                            isMatchSku = isMatchSku && skuColorKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(goodDetail.getSpecList().stream().filter(propItem -> propItem.getK().contains("颜色") || propItem.getK().contains("规格")), keyword, false));
                        }
                        //尺码可能没有标签
                        if(goodDetail.getSpecList().stream().anyMatch(propItem -> propItem.getK().contains("尺码") || propItem.getK().trim().isEmpty()) && skuSizeKeyWords != null && !skuSizeKeyWords.isEmpty()){
                            //尺寸、尺码
                            isMatchSku = isMatchSku && skuSizeKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(goodDetail.getSpecList().stream().filter(propItem -> propItem.getK().contains("尺码") || propItem.getK().trim().isEmpty()), keyword, true));
                        }
                        if(goodDetail.getSpecList().stream().anyMatch(propItem -> propItem.getK().contains("款式")) && skuStyleKeyWords != null && !skuStyleKeyWords.isEmpty()){
                            isMatchSku = isMatchSku && skuStyleKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(goodDetail.getSpecList().stream().filter(propItem -> propItem.getK().contains("款式")), keyword, false));
                        }
                        if(goodDetail.getSpecList().stream().anyMatch(propItem -> propItem.getK().contains("规格")) && skuSpecKeyWords != null && !skuSpecKeyWords.isEmpty()){
                            isMatchSku = isMatchSku && skuSpecKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(goodDetail.getSpecList().stream().filter(propItem -> propItem.getK().contains("规格")), keyword, false));
                        }
                    }
                    //finalToBuyGoodInfo.setGoodsId(goodDetail.getGoodsId());
                    //五分钟内再开抢
                    boolean isTimeMatch = goodItem.getShiftedOnStamp() - System.currentTimeMillis() < 300000;
                    if(isMatchSku){
                        System.out.println(TimeUtil.getCurrentTimeString() + " attrsku match: "+goodItem.getGoodsId()+", shopName:"+ goodItem.getName());
                        ShoppingForAppDTO.GoodDataStockDetailDTO tmpToBuy = new ShoppingForAppDTO.GoodDataStockDetailDTO();
                        tmpToBuy.setMainGoodsId(goodItem.getGoodsId());
                        tmpToBuy.setName(goodItem.getName());
                        tmpToBuy.setGoodsId(goodDetail.getGoodsId());
                        tmpToBuy.setToBuyNum(goodDetail.getInventory() > toBuyNum ? toBuyNum : goodDetail.getInventory());
                        if(!Optional.ofNullable(FileOperation.stockInfoMap.get(goodItem.getGoodsId())).orElse(false)){
                            FileOperation.writeFileByAppend(TimeUtil.getCurrentTimeString() + ": "+goodItem.getGoodsId()+": "+goodDataInfo, ConstInfoForApp.mageStockInfoFileName);
                            FileOperation.stockInfoMap.putIfAbsent(goodItem.getGoodsId(), true);
                        }
                        if(!isTimeMatch){
                            System.out.println(goodItem.getName()+" 开抢时间："+ goodItem.getShiftedOn()+" 还未到,暂不加入代下单列表");
                            return;
                        }
                        realToBuyGoodList.add(tmpToBuy);
                        if(mainGoodIdPromotionIdsMap.containsKey(tmpToBuy.getMainGoodsId())){
                            tmpToBuy.setPromotionIdList(mainGoodIdPromotionIdsMap.get(tmpToBuy.getMainGoodsId()));
                            tmpToBuy.setPromotionIdStr(CollectionUtils.isEmpty(mainGoodIdPromotionIdsMap.get(tmpToBuy.getMainGoodsId())) ? "" : String.join(",",mainGoodIdPromotionIdsMap.get(tmpToBuy.getMainGoodsId())));
                        }
                        //finalToBuyGoodInfo.setGoodsId(goodDetail.getGoodsId());
                        //finalToBuyGoodInfo.setToBuyNum(goodDetail.getInventory() > toBuyNum ? toBuyNum : goodDetail.getInventory());
                    }
                });
            }

        }catch (Exception ex){
            ex.printStackTrace();
            operationInfo.append(ex.getMessage());
        }
        if(!operationInfo.toString().isEmpty()){
            System.out.println(TimeUtil.getCurrentTimeString() + "getToBuyGoodSkuInfoDetail operationInfo: "+ operationInfo.toString());
        }
    }

    /**
     * 根据商品主id更新对应优惠信息
     * @param mainGoodsId
     * @param goodDataDetail
     */
    private static void updateMainGoodIdAndPromotionInfo(String mainGoodsId, ShoppingForAppDTO.GoodDataDetailDTO goodDataDetail) {
        if(goodDataDetail != null){
            mainGoodIdPromotionIdsMap.put(mainGoodsId, new ArrayList<>(Arrays.asList(goodDataDetail.getActivityList())));
        }
    }

    private static boolean isSkuItemListMatch(Stream<ToBuyGoodInfoAppDTO.KV> propList, String keyword, boolean strictEqual) {
        return propList.allMatch(item -> isSkuItemMatch(item,keyword,strictEqual));
    }

    private static boolean isSkuItemMatch(ToBuyGoodInfoAppDTO.KV item, String keyword, boolean strictEqual) {
        if(strictEqual){
            if(item.getV().contains("-")){
                return item.getV().contains(keyword);
            }
            return keyword.equalsIgnoreCase(item.getV());
        }
        return isSkuItemMatch(item.getV(),keyword);
    }
    private static boolean isSkuItemMatch(String skuItem, String keyword) {
        if (skuItem.contains(keyword)) {
            return true;
        }
        if (keyword != null && keyword.contains("+")) {
            List<String> keywordList = Arrays.asList(keyword.split("\\+"));
            if(keyword.replaceAll("\\+"," ").equalsIgnoreCase(skuItem)){
                return true;
            }
            if (keywordList.stream().allMatch(key -> skuItem.contains(key.trim()))) {
                return true;
            }
        }
        if (keyword != null && keyword.contains("|")) {
            List<String> keywordList = Arrays.asList(keyword.split("\\|"));
            if(keyword.replaceAll("\\|"," ").equalsIgnoreCase(skuItem)){
                return true;
            }
            if (keywordList.stream().allMatch(key -> skuItem.contains(key.trim()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 重新填充商品对应的属性信息，方便匹配
     *
     * @param goodDataDetail
     */
    private static void resetGoodDetailSpecListInfo(ShoppingForAppDTO.GoodDataDetailDTO goodDataDetail) {
        if(goodDataDetail.getSpecList() == null || goodDataDetail.getSpecList().isEmpty()){
            return;
        }
        goodDataDetail.getGoodsList().forEach(good -> {
            if(Optional.ofNullable(good.getInventory()).orElse(0) > 0){
                good.setSpecList(new ArrayList<>());
                if(good.getSpec1() != null && !good.getSpec1().isEmpty()){
                    good.getSpecList().add(new ToBuyGoodInfoAppDTO.KV(goodDataDetail.getSpecList().get(0).getTitle(),good.getSpec1()));
                }
                if(good.getSpec2() != null && !good.getSpec2().isEmpty() && goodDataDetail.getSpecList().size() >= 2){
                    good.getSpecList().add(new ToBuyGoodInfoAppDTO.KV(goodDataDetail.getSpecList().get(1).getTitle(),good.getSpec2()));
                }
                if(good.getSpec3() != null && !good.getSpec3().isEmpty() && goodDataDetail.getSpecList().size() >= 3){
                    good.getSpecList().add(new ToBuyGoodInfoAppDTO.KV(goodDataDetail.getSpecList().get(2).getTitle(),good.getSpec3()));
                }
                if(good.getSpec4() != null && !good.getSpec4().isEmpty() && goodDataDetail.getSpecList().size() >= 4){
                    good.getSpecList().add(new ToBuyGoodInfoAppDTO.KV(goodDataDetail.getSpecList().get(3).getTitle(),good.getSpec4()));
                }
            }
        });
    }

    /**
     * 下单
     *  @param toBuyGoodAndAddressInfos
     * @param auth
     * @param memberId
     */
    public static void commitToBuyOrder(List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> toBuyGoodAndAddressInfos, String auth, String memberId) {
        StringBuilder operationInfo = new StringBuilder("");
        /*if(AutoShoppingEntryForWeChatApp.mapOrderedCounter.get(memberId) <= 0){
            System.out.println(TimeUtil.getCurrentTimeString() +": stop commit order for counter limit 0!");
            return;
        }*/
        if(toBuyGoodAndAddressInfos != null){
            List<Future<String>> commitOrderFutures = new ArrayList<>();
            toBuyGoodAndAddressInfos.forEach(toBuy -> {
                if(toBuy.getAddressDetailInfo() != null && toBuy.getToBuyGoodInfoList() != null && !toBuy.getToBuyGoodInfoList().isEmpty()){
                    toBuy.getToBuyGoodInfoList().forEach(buyGood -> {
                        //已下单排除
                        boolean canCommitOrder = buyGood != null && buyGood.getGoodsId() != null && !buyGood.getGoodsId().isEmpty() && Optional.ofNullable(buyGood.getToBuyNum()).orElse(0) > 0 && (buyGood.getOrderNo() == null || buyGood.getOrderNo().isEmpty());
                        if(canCommitOrder){
                            commitOrderFutures.add(executorServiceForCommitOrder.submit(() -> RequestBllForApp.commitOrderDetail(commitOrderUrlForApp, toBuy, buyGood, RequestBllForApp.getCommitPostEntity(buyGood, toBuy.getAddressDetailInfo(), buyGood.getToBuyNum()), auth, 0, memberId)));
                        }
                    });
                    //有多个优惠同时走购物车下单
                    Map<String, List<ShoppingForAppDTO.GoodDataStockDetailDTO>> promotionGroup = toBuy.getToBuyGoodInfoList().stream().collect(Collectors.groupingBy(ShoppingForAppDTO.GoodDataStockDetailDTO::getPromotionIdStr));
                    promotionGroup.entrySet().forEach(entry -> {
                        if(!StringUtils.isEmpty(entry.getKey()) && entry.getValue().size() > 1){
                            System.out.println("优惠提交购物车："+ entry.getValue().toString());
                            commitOrderForShoppingCar(entry.getValue(), toBuy.getAddressDetailInfo(), auth, memberId);
                        }
                    });
                }
            });
            commitOrderFutures.forEach(task -> {
                try {
                    String commitResult = task.get();
                    System.out.println(TimeUtil.getCurrentTimeString() + commitResult);
                    if(commitResult != null && (commitResult.contains("下单太频繁") || commitResult.contains("系统繁忙"))){
                        Thread.sleep(500);
                        System.out.println(TimeUtil.getCurrentTimeString() +  Thread.currentThread().getName()+" 暂停500 ");
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    operationInfo.append(ex.getMessage());
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                    operationInfo.append(ex.getMessage());
                }
            });
        }
        if(!operationInfo.toString().isEmpty()){
            System.out.println(TimeUtil.getCurrentTimeString() + "commitToBuyOrder operationInfo: "+ operationInfo.toString());
        }
    }

    /**
     * 购物车下单
     *  @param preToBuyGoodList
     * @param addressDetailInfo
     * @param auth
     * @param memberId
     */
    private static String commitOrderForShoppingCar(List<ShoppingForAppDTO.GoodDataStockDetailDTO> preToBuyGoodList, ShoppingForAppDTO.AddressDataRowDetailDTO addressDetailInfo, String auth, String memberId) {
        StringEntity commitPostEntity = RequestBllForApp.getPromotionPostEntity(preToBuyGoodList, addressDetailInfo);
        //String addShoppingCarInfo = doPost(MessageFormat.format(shoppingCarUrlForApp,memberId), commitPostEntity, auth);
        String operationInfo = "";
        String orderInfo = "";
        try {
            orderInfo = doPostForShoppingCar(commitOrderUrlForApp, commitPostEntity, auth);
        }catch (Exception ex){
            operationInfo = ex.getMessage();
            try {
                ShoppingForAppDTO.CommitOrderErrorDTO commitOrderError = JSONObject.parseObject(orderInfo, ShoppingForAppDTO.CommitOrderErrorDTO.class);
                System.out.println(TimeUtil.getCurrentTimeString() + " commitOrderForShoppingCarError: " + commitOrderError.toString());
                if(commitOrderError != null){
                    //80005 未开始抢，这个时候不用重新查询库存信息。 多抢几次再查询
                    int count = 0;
                    while (Optional.ofNullable(commitOrderError.getCode()).orElse(0) == 80005 && count<6){
                        commitOrderError = JSONObject.parseObject(commitOrderForShoppingCarV2(commitPostEntity, auth),ShoppingForAppDTO.CommitOrderErrorDTO.class);
                        count++;
                        System.out.println(count);
                    }
                    //80001提交下单后提示库存不足，重复提交多次，不用重新查询库存
                    while (Optional.ofNullable(commitOrderError.getCode()).orElse(0) == 80001 && count<10){
                        commitOrderError = JSONObject.parseObject(commitOrderForShoppingCarV2(commitPostEntity, auth),ShoppingForAppDTO.CommitOrderErrorDTO.class);
                        count++;
                        System.out.println(count);
                    }
                }
            }catch (Exception ex1){
                ex1.printStackTrace();
            }

            if(!operationInfo.isEmpty()){
                System.out.println(TimeUtil.getCurrentTimeString() +" commitOrderForShoppingCarV2 operationInfo: "+ operationInfo);
            }
        }
        return orderInfo;
    }

    private static String commitOrderForShoppingCarV2(StringEntity commitPostEntity, String auth) {
        String operationInfo = "";
        String orderInfo = "";
        try {
            orderInfo = doPostForShoppingCar(commitOrderUrlForApp, commitPostEntity, auth);
        }catch (Exception ex){
            operationInfo = ex.getMessage();
            try {
                ShoppingForAppDTO.CommitOrderErrorDTO commitOrderError = JSONObject.parseObject(orderInfo, ShoppingForAppDTO.CommitOrderErrorDTO.class);
                System.out.println(TimeUtil.getCurrentTimeString() + " commitOrderError: " + commitOrderError.toString());
                if(commitOrderError != null){
                    //80005 未开始抢，这个时候不用重新查询库存信息。 多抢几次再查询
                    int count = 0;
                    while (Optional.ofNullable(commitOrderError.getCode()).orElse(0) == 80005 && count<6){
                        commitOrderError = JSONObject.parseObject(commitOrderForShoppingCarV2(commitPostEntity, auth),ShoppingForAppDTO.CommitOrderErrorDTO.class);
                        count++;
                        System.out.println(count);
                    }
                    //80001提交下单后提示库存不足，重复提交多次，不用重新查询库存
                    while (Optional.ofNullable(commitOrderError.getCode()).orElse(0) == 80001 && count<10){
                        commitOrderError = JSONObject.parseObject(commitOrderForShoppingCarV2(commitPostEntity, auth),ShoppingForAppDTO.CommitOrderErrorDTO.class);
                        count++;
                        System.out.println(count);
                    }
                }
            }catch (Exception ex1){
                ex1.printStackTrace();
            }

            if(!operationInfo.isEmpty()){
                System.out.println(TimeUtil.getCurrentTimeString() +" commitOrderForShoppingCarV2 operationInfo: "+ operationInfo);
            }
        }
        return orderInfo;
    }

    /**
     * 移除已购商品，避免重复下单
     * 根据已购订单号存在即下单成功->所有待抢购商品都抢到
     *
     * @param toBuyGoodAndAddressInfos
     * @param alreadyBuyGoodInfo
     * @param intendToBuyGoods
     */
    public static void removeAlreadyBuyAndToPayGood(List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> toBuyGoodAndAddressInfos, List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> alreadyBuyGoodInfo, List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> intendToBuyGoods) {
        String operationInfo = "";
        if (toBuyGoodAndAddressInfos != null && !toBuyGoodAndAddressInfos.isEmpty()) {
            try {
                toBuyGoodAndAddressInfos.stream().filter(item -> item.getToBuyGoodInfoList() != null && !item.getToBuyGoodInfoList().isEmpty()).forEach(toBuy -> toBuy.setReadyToBuyGoodNum(toBuy.getToBuyGoodInfoList().stream().filter(toBuyItem -> toBuyItem.getOrderNo() != null && !toBuyItem.getOrderNo().isEmpty()).count()));
                alreadyBuyGoodInfo.addAll(toBuyGoodAndAddressInfos.stream().filter(item -> item.getCommitOrderInfoList() != null && !item.getCommitOrderInfoList().isEmpty() && item.getToBuyGoodInfoList() != null && !item.getToBuyGoodInfoList().isEmpty() && item.getCommitOrderInfoList().stream().filter(commitOrder -> commitOrder.getData() != null && commitOrder.getData().getOrderId() != null && !commitOrder.getData().getOrderId().isEmpty()).count() > 0/*== item.getReadyToBuyGoodNum()*/).collect(Collectors.toList()));
                List<String> alreadyBuyLocalNos = alreadyBuyGoodInfo.stream().map(ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO::getLocalNo).collect(Collectors.toList());
                toBuyGoodAndAddressInfos.removeIf(item -> item.getCommitOrderInfoList() != null && !item.getCommitOrderInfoList().isEmpty() && item.getToBuyGoodInfoList() != null && !item.getToBuyGoodInfoList().isEmpty() && item.getCommitOrderInfoList().stream().filter(commitOrder -> commitOrder.getData() != null && commitOrder.getData().getOrderId() != null && !commitOrder.getData().getOrderId().isEmpty()).count() > 0 /*item.getReadyToBuyGoodNum()*/);
                if(alreadyBuyLocalNos != null && !alreadyBuyLocalNos.isEmpty()){
                    String messageResult = WeChatBll.sendMessage("order success:" + intendToBuyGoods.stream().filter(intend -> alreadyBuyLocalNos.contains(intend.getLocalNo())).findFirst().orElse(new ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO()).getDesc(), Arrays.asList(qqOpenId, zzOpenId,zzjjOpenId));
                    intendToBuyGoods.removeIf(item -> alreadyBuyLocalNos.contains(item.getLocalNo()));
                    if(messageResult != null && !messageResult.isEmpty()){
                        System.out.println(TimeUtil.getCurrentTimeString() + "toBuyGoodAndAddressInfos: "+toBuyGoodAndAddressInfos.toString());
                        System.out.println(TimeUtil.getCurrentTimeString() + "alreadyBuyGoodInfo: "+alreadyBuyGoodInfo.toString());
                        System.out.println(TimeUtil.getCurrentTimeString() + "intendToBuyGoods: "+intendToBuyGoods.toString());
                    }
                }

            }catch (Exception ex){
                ex.printStackTrace();
                operationInfo += ex.getMessage();
            }
        }
        if(!operationInfo.isEmpty()){
            System.out.println(TimeUtil.getCurrentTimeString() + "removeAlreadyBuyAndToPayGood operationInfo: "+ operationInfo);
        }
    }
}
