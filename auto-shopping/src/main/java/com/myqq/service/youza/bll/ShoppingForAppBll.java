package com.myqq.service.youza.bll;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.entity.ShoppingForAppDTO;
import com.myqq.service.youza.entity.ToBuyGoodInfoAppDTO;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.myqq.service.youza.constinfo.ConstInfoForApp.*;

/**
 * baiqiujie app
 */
public class ShoppingForAppBll {
    static final List<ShoppingForAppDTO.GoodDataStockDetailDTO> preToBuyGoodList = new ArrayList<>(50);
    /**
     * 线程池
     */
    public static ThreadPoolExecutor executorServiceForGoodDetail = new ThreadPoolExecutor (5, 20, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100)) {
    };
    public static ThreadPoolExecutor executorServiceForCommitOrder = new ThreadPoolExecutor (3, 10, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100)) {
    };

    public static ThreadPoolExecutor executorServiceForCancelOrder = new ThreadPoolExecutor (3, 10, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100)) {
    };

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
        String goodsInfo = RequestBllForApp.doGet(getGoodListUrlForApp, auth);
        List<Future<Boolean>> taskList = new ArrayList<>();
        toBuyGoodAndAddressInfos.forEach(goodInfo -> {
            if(goodInfo != null && goodInfo.getAddressDetailInfo() != null ){
                String shotShopName = goodInfo.getShotGoodName();
                List<String> skuColorKeyWords = goodInfo.getGoodColorKeyWords();
                List<String> skuSizeKeyWords = goodInfo.getGoodSizeKeyWords();
                List<String> skuStyleKeyWords = goodInfo.getGoodStyleKeyWords();
                List<String> skuSpecKeyWords = goodInfo.getGoodSpecKeyWords();

                List<ShoppingForAppDTO.GoodDataStockDetailDTO> realToBuyGoodList = new ArrayList<>(10);
                if(goodInfo.getToBuyGoodInfoList() == null){
                    goodInfo.setToBuyGoodInfoList(realToBuyGoodList);
                }
                ShoppingForAppDTO.GoodsListDTO goodsList = JSONObject.parseObject(goodsInfo, ShoppingForAppDTO.GoodsListDTO.class);
                if(goodsList != null && goodsList.getData() != null && goodsList.getData().getRows() != null){
                    //非预售 TODO 后续区分
                    goodsList.getData().getRows().stream().filter(item -> item.getInventoryAmount() > 0).forEach(item -> {
                        if(shotShopName != null && !shotShopName.isEmpty()){
                            if(item.getName().contains(shotShopName)){
                                ShoppingForAppDTO.GoodDataStockDetailDTO toBuyGoodInfo = realToBuyGoodList.stream().filter(good -> good.getMainGoodsId().equalsIgnoreCase(item.getGoodsId())).findFirst().orElse(null);
                                if(toBuyGoodInfo == null){
                                    toBuyGoodInfo = new ShoppingForAppDTO.GoodDataStockDetailDTO();
                                    //realToBuyGoodList.add(toBuyGoodInfo);
                                    toBuyGoodInfo.setMainGoodsId(item.getGoodsId());
                                    toBuyGoodInfo.setName(item.getName());

                                    ShoppingForAppDTO.GoodDataStockDetailDTO finalToBuyGoodInfo1 = toBuyGoodInfo;
                                    System.out.println(AutoShoppingEntryForApp.dateTimeFormatter.format(LocalDateTime.now()) +" getToBuyGoodSkuInfoDetail buy new :"+JSONObject.toJSONString(goodInfo));
                                    taskList.add(executorServiceForGoodDetail.submit(() -> {getToBuyGoodSkuInfoDetail(item, finalToBuyGoodInfo1, skuColorKeyWords,skuSizeKeyWords, skuStyleKeyWords, skuSpecKeyWords, goodInfo.getToBuyNum(), auth, realToBuyGoodList); return Boolean.TRUE; }));
                                }else {
                                    ShoppingForAppDTO.GoodDataStockDetailDTO finalToBuyGoodInfo = toBuyGoodInfo;
                                    System.out.println(AutoShoppingEntryForApp.dateTimeFormatter.format(LocalDateTime.now())+" getToBuyGoodSkuInfoDetail update:"+JSONObject.toJSONString(goodInfo));
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
            System.out.println("buildToBuyGoodInfo operationInfo: "+ operationInfo.toString());
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
    private static void getToBuyGoodSkuInfoDetail(ShoppingForAppDTO.GoodsListDataRowDTO goodItem, ShoppingForAppDTO.GoodDataStockDetailDTO finalToBuyGoodInfo, List<String> skuColorKeyWords, List<String> skuSizeKeyWords, List<String> skuStyleKeyWords, List<String> skuSpecKeyWords, int toBuyNum, String auth, List<ShoppingForAppDTO.GoodDataStockDetailDTO> realToBuyGoodList) {
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
                        if(goodDetail.getSpecList().stream().anyMatch(propItem -> propItem.getK().contains("颜色")) && skuColorKeyWords != null && !skuColorKeyWords.isEmpty()){
                            isMatchSku = isMatchSku && skuColorKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(goodDetail.getSpecList().stream().filter(propItem -> propItem.getK().contains("颜色")), keyword, false));
                        }
                        if(goodDetail.getSpecList().stream().anyMatch(propItem -> propItem.getK().contains("尺码")) && skuSizeKeyWords != null && !skuSizeKeyWords.isEmpty()){
                            isMatchSku = isMatchSku && skuSizeKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(goodDetail.getSpecList().stream().filter(propItem -> propItem.getK().contains("尺码")), keyword, true));
                        }
                        if(goodDetail.getSpecList().stream().anyMatch(propItem -> propItem.getK().contains("款式")) && skuStyleKeyWords != null && !skuStyleKeyWords.isEmpty()){
                            isMatchSku = isMatchSku && skuStyleKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(goodDetail.getSpecList().stream().filter(propItem -> propItem.getK().contains("款式")), keyword, false));
                        }
                        if(goodDetail.getSpecList().stream().anyMatch(propItem -> propItem.getK().contains("规格")) && skuSpecKeyWords != null && !skuSpecKeyWords.isEmpty()){
                            isMatchSku = isMatchSku && skuSpecKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(goodDetail.getSpecList().stream().filter(propItem -> propItem.getK().contains("规格")), keyword, false));
                        }
                    }
                    //finalToBuyGoodInfo.setGoodsId(goodDetail.getGoodsId());
                    if(isMatchSku){
                        ShoppingForAppDTO.GoodDataStockDetailDTO tmpToBuy = new ShoppingForAppDTO.GoodDataStockDetailDTO();
                        tmpToBuy.setMainGoodsId(goodItem.getGoodsId());
                        tmpToBuy.setName(goodItem.getName());
                        tmpToBuy.setGoodsId(goodDetail.getGoodsId());
                        tmpToBuy.setToBuyNum(goodDetail.getInventory() > toBuyNum ? toBuyNum : goodDetail.getInventory());
                        realToBuyGoodList.add(tmpToBuy);
                        //finalToBuyGoodInfo.setGoodsId(goodDetail.getGoodsId());
                        //finalToBuyGoodInfo.setToBuyNum(goodDetail.getInventory() > toBuyNum ? toBuyNum : goodDetail.getInventory());
                    }
                });
            }

        }catch (Exception ex){
            operationInfo.append(ex.getMessage());
        }
        if(!operationInfo.toString().isEmpty()){
            System.out.println("getToBuyGoodSkuInfoDetail operationInfo: "+ operationInfo.toString());
        }
    }
    private static boolean isSkuItemListMatch(Stream<ToBuyGoodInfoAppDTO.KV> propList, String keyword, boolean strictEqual) {
        return propList.allMatch(item -> isSkuItemMatch(item,keyword,strictEqual));
    }

    private static boolean isSkuItemMatch(ToBuyGoodInfoAppDTO.KV item, String keyword, boolean strictEqual) {
        if(strictEqual){
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
     *
     * @param toBuyGoodAndAddressInfos
     * @param auth
     */
    public static void commitToBuyOrder(List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> toBuyGoodAndAddressInfos, String auth) {
        StringBuilder operationInfo = new StringBuilder("");
        if(toBuyGoodAndAddressInfos != null){
            List<Future<String>> commitOrderFutures = new ArrayList<>();
            toBuyGoodAndAddressInfos.forEach(toBuy -> {
                if(toBuy.getAddressDetailInfo() != null && toBuy.getToBuyGoodInfoList() != null && !toBuy.getToBuyGoodInfoList().isEmpty()){
                    toBuy.getToBuyGoodInfoList().forEach(buyGood -> {
                        //已下单排除
                        boolean canCommitOrder = buyGood != null && buyGood.getGoodsId() != null && !buyGood.getGoodsId().isEmpty() && Optional.ofNullable(buyGood.getToBuyNum()).orElse(0) > 0 && (buyGood.getOrderNo() == null || buyGood.getOrderNo().isEmpty());
                        if(canCommitOrder){
                            commitOrderFutures.add(executorServiceForCommitOrder.submit(() -> RequestBllForApp.commitOrderDetail(toBuy, buyGood, RequestBllForApp.getCommitPostEntity(buyGood, toBuy.getAddressDetailInfo(), buyGood.getToBuyNum()), auth)));
                        }
                    });
                }
            });
            commitOrderFutures.forEach(task -> {
                try {
                    String commitResult = task.get();
                    System.out.println(commitResult);
                    if(commitResult != null && (commitResult.contains("下单太频繁") || commitResult.contains("系统繁忙"))){
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName()+" 暂停1000 ");
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
            System.out.println("commitToBuyOrder operationInfo: "+ operationInfo.toString());
        }
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
                alreadyBuyGoodInfo.addAll(toBuyGoodAndAddressInfos.stream().filter(item -> item.getCommitOrderInfoList() != null && !item.getCommitOrderInfoList().isEmpty() && item.getToBuyGoodInfoList() != null && !item.getToBuyGoodInfoList().isEmpty() && item.getCommitOrderInfoList().stream().filter(commitOrder -> commitOrder.getData() != null && commitOrder.getData().getOrderId() != null && !commitOrder.getData().getOrderId().isEmpty()).count() == item.getReadyToBuyGoodNum()).collect(Collectors.toList()));
                List<String> alreadyBuyLocalNos = alreadyBuyGoodInfo.stream().map(ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO::getLocalNo).collect(Collectors.toList());
                toBuyGoodAndAddressInfos.removeIf(item -> item.getCommitOrderInfoList() != null && !item.getCommitOrderInfoList().isEmpty() && item.getToBuyGoodInfoList() != null && !item.getToBuyGoodInfoList().isEmpty() && item.getCommitOrderInfoList().stream().filter(commitOrder -> commitOrder.getData() != null && commitOrder.getData().getOrderId() != null && !commitOrder.getData().getOrderId().isEmpty()).count() >= item.getReadyToBuyGoodNum());
                if(alreadyBuyLocalNos != null && !alreadyBuyLocalNos.isEmpty()){
                    WeChatBll.sendMessage("order success:"+intendToBuyGoods.stream().filter(intend -> alreadyBuyLocalNos.contains(intend.getLocalNo())).findFirst().orElse(new ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO()).getDesc());
                    intendToBuyGoods.removeIf(item -> alreadyBuyLocalNos.contains(item.getLocalNo()));
                }

            }catch (Exception ex){
                ex.printStackTrace();
                operationInfo += ex.getMessage();
            }
        }
        if(!operationInfo.isEmpty()){
            System.out.println("removeAlreadyBuyAndToPayGood operationInfo: "+ operationInfo);
        }
    }
}
