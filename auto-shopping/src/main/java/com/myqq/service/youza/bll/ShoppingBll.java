package com.myqq.service.youza.bll;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.entity.StringEntity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.myqq.service.youza.bll.ConstInfo.*;
import static com.myqq.service.youza.bll.ShopInfoDTO.*;

public class ShoppingBll {
    static final List<ToBuyGoodInfo> preToBuyGoodList = new ArrayList<>(50);
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
     * 下单
     * @param buyUrl
     * @param toBuyGoodAndAddressInfos
     */
    public static void commitToBuyOrder(String buyUrl, List<ToBuyGoodAndAddressInfo> toBuyGoodAndAddressInfos,String kdtSession,String kdtID) {
        if(toBuyGoodAndAddressInfos != null){
            List<Future<String>> commitOrderFutures = new ArrayList<>();
            toBuyGoodAndAddressInfos.forEach(toBuy -> {
                if(toBuy.getAddressDetailInfo() != null && toBuy.getToBuyGoodInfoList() != null && !toBuy.getToBuyGoodInfoList().isEmpty()){
                    toBuy.getToBuyGoodInfoList().forEach(buyGood -> {
                        if(buyGood != null && buyGood.toBuyGoodSkuInfos != null){
                            buyGood.toBuyGoodSkuInfos.forEach(toBuyGoodSkuInfo -> {
                                if(toBuyGoodSkuInfo.orderNo == null || toBuyGoodSkuInfo.orderNo.isEmpty()){
                                    commitOrderFutures.add(executorServiceForCommitOrder.submit(() -> RequestBll.commitOrderDetail(toBuy,buyUrl,toBuyGoodSkuInfo,getCommitPostEntityFromAddress(buyGood, toBuyGoodSkuInfo,toBuy.getAddressDetailInfo(),kdtSession,kdtID),kdtSession,kdtID)));
                                }
                            });
                        }
                    });
                }
            });
            commitOrderFutures.forEach(task -> {
                try {
                    String commitResult = task.get();
                    System.out.println(commitResult);
                    if(commitResult != null && commitResult.contains("下单太频繁")){
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName()+" 暂停1000 ");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static StringEntity getCommitPostEntityFromAddress(ToBuyGoodInfo buyGood, ToBuyGoodSkuInfo toBuyGoodSkuInfo, AddressDetailInfoDTO addressDetailInfo,String kdtSession, String kdtID) {
        StringEntity entity = null;
        try {
            String diliveryContent = "{\n" +
                    "    \"source\": {\n" +
                    "        \"bookKey\": \"aade1582-d633-4ced-9fd8-2de5605249b5\",\n" +
                    "        \"clientIp\": \"211.161.244.158\",\n" +
                    "        \"fromThirdApp\": false,\n" +
                    "        \"kdtSessionId\": \"%s\",\n" +
                    "        \"needAppRedirect\": false,\n" +
                    "        \"orderFrom\": \"\",\n" +
                    "        \"orderType\": 0,\n" +
                    "        \"platform\": \"weixin\",\n" +
                    "        \"salesman\": \"\",\n" +
                    "        \"userAgent\": \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat QBCore/3.43.1021.400 QQBrowser/9.0.2524.400\",\n" +
                    "        \"bizPlatform\": \"\"\n" +
                    "    },\n" +
                    "    \"config\": {\n" +
                    "        \"receiveMsg\": true\n" +
                    "    },\n" +
                    "    \"seller\": {\n" +
                    "        \"kdtId\": %s,\n" +
                    "        \"storeId\": 0\n" +
                    "    },\n" +
                    "    \"delivery\": {\n" +
                    "        \"hasFreightInsurance\": false,\n" +
                    "        \"address\": {\n" +
                    "            \"addressDetail\": \"%s\",\n" +
                    "            \"areaCode\": \"%s\",\n" +
                    "            \"city\": \"%s\",\n" +
                    "            \"community\": \"\",\n" +
                    "            \"country\": \"中国\",\n" +
                    "            \"countryType\": 1,\n" +
                    "            \"county\": \"%s\",\n" +
                    "            \"id\": %d,\n" +
                    "            \"isDefault\": 1,\n" +
                    "            \"lat\": \"%s\",\n" +
                    "            \"lon\": \"%s\",\n" +
                    "            \"postalCode\": \"%s\",\n" +
                    "            \"province\": \"%s\",\n" +
                    "            \"source\": 1,\n" +
                    "            \"tel\": \"%s\",\n" +
                    "            \"userId\": %d,\n" +
                    "            \"userName\": \"%s\",\n" +
                    "            \"recipients\": \"%s\",\n" +
                    "            \"addressId\": %d\n" +
                    "        },\n" +
                    "        \"expressType\": \"express\",\n" +
                    "        \"expressTypeChoice\": 0\n" +
                    "    }";
            StringBuilder commitOrderContentSB = new StringBuilder(String.format(diliveryContent,kdtSession,kdtID,addressDetailInfo.addressDetail,addressDetailInfo.areaCode,addressDetailInfo.city,addressDetailInfo.county,addressDetailInfo.id,addressDetailInfo.lat,addressDetailInfo.lon,addressDetailInfo.postalCode,addressDetailInfo.province,addressDetailInfo.tel,addressDetailInfo.userId,addressDetailInfo.userName,addressDetailInfo.userName,addressDetailInfo.id));

            String goodInfo = ",\n" +
                    "    \"items\": [\n" +
                    "        {\n" +
                    "            \"goodsId\": %d,\n" +
                    "            \"num\": %d,\n" +
                    "            \"skuId\": %d\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            if(buyGood != null ){
                commitOrderContentSB.append(String.format(goodInfo,buyGood.goodsId,toBuyGoodSkuInfo.num,toBuyGoodSkuInfo.skuId));
            }
            entity = new StringEntity(commitOrderContentSB.toString(),"utf-8");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return entity;
    }

    /**
     * 抢购商品信息
     * @param i
     * @param toBuyGoodAndAddressInfos
     */
    public static void buildToBuyGoodInfo(long i, List<ToBuyGoodAndAddressInfo> toBuyGoodAndAddressInfos,String kdtSession,String kdtId,String shopId, String goodsTagId) {
        long startTime = System.currentTimeMillis();
        if(toBuyGoodAndAddressInfos == null || toBuyGoodAndAddressInfos.isEmpty()){
            return;
        }
        String goodsInfo = RequestBll.doGet(MessageFormat.format(getgoodListUrl,shopId,goodsTagId),kdtSession,kdtId);
        List<Future<Boolean>> taskList = new ArrayList<>();
        toBuyGoodAndAddressInfos.forEach(goodInfo -> {
            if(goodInfo != null && goodInfo.getAddressDetailInfo() != null ){
                String shotShopName = goodInfo.getShotGoodName();
                List<String> skuColorKeyWords = goodInfo.getGoodColorKeyWords();
                List<String> skuSizeKeyWords = goodInfo.getGoodSizeKeyWords();
                List<String> skuStyleKeyWords = goodInfo.getGoodStyleKeyWords();
                List<String> skuSpecKeyWords = goodInfo.getGoodSpecKeyWords();

                List<ToBuyGoodInfo> realToBuyGoodList = new ArrayList<>(10);
                if(goodInfo.getToBuyGoodInfoList() == null){
                    goodInfo.setToBuyGoodInfoList(realToBuyGoodList);
                }
                GoodsList goodsList = JSONObject.parseObject(goodsInfo, GoodsList.class);
                if(goodsList != null && goodsList.data != null && goodsList.data.list != null){
                    //非预售
                    goodsList.data.list.stream().filter(data -> data.pre_sale == goodInfo.isNeedPresale()).forEach(item -> {
                        if(shotShopName != null && !shotShopName.isEmpty()){
                            if(item.title.contains(shotShopName)){
                                ToBuyGoodInfo toBuyGoodInfo = realToBuyGoodList.stream().filter(good -> good.goodsId.equals(item.id)).findFirst().orElse(null);
                                if(toBuyGoodInfo == null){
                                    toBuyGoodInfo = new ToBuyGoodInfo();
                                    realToBuyGoodList.add(toBuyGoodInfo);
                                    toBuyGoodInfo.goodsId = item.id;
                                    toBuyGoodInfo.googsName = item.title;
                                    toBuyGoodInfo.toBuyGoodSkuInfos = new ArrayList<>();

                                    ToBuyGoodInfo finalToBuyGoodInfo1 = toBuyGoodInfo;
                                    //getToBuyGoodSkuInfoDetail(item, finalToBuyGoodInfo1,skuColorKeyWords,skuSizeKeyWords,skuStyleKeyWords,kdtSession,goodInfo.getToBuyNum());
                                    System.out.println("getToBuyGoodSkuInfoDetail buy new :"+JSONObject.toJSONString(goodInfo));
                                    taskList.add(executorServiceForGoodDetail.submit(() -> {getToBuyGoodSkuInfoDetail(item, finalToBuyGoodInfo1,skuColorKeyWords,skuSizeKeyWords,skuStyleKeyWords,skuSpecKeyWords,kdtSession,goodInfo.getToBuyNum(),kdtId,shopId);return Boolean.TRUE; }));
                                }else {
                                    ToBuyGoodInfo finalToBuyGoodInfo = toBuyGoodInfo;
                                    //getToBuyGoodSkuInfoDetail(item, finalToBuyGoodInfo,skuColorKeyWords,skuSizeKeyWords,skuStyleKeyWords,kdtSession,goodInfo.getToBuyNum());
                                    System.out.println("getToBuyGoodSkuInfoDetail update:"+JSONObject.toJSONString(goodInfo));
                                    taskList.add(executorServiceForGoodDetail.submit(() -> {getToBuyGoodSkuInfoDetail(item, finalToBuyGoodInfo,skuColorKeyWords,skuSizeKeyWords,skuStyleKeyWords,skuSpecKeyWords,kdtSession,goodInfo.getToBuyNum(),kdtId,shopId); return true;}));
                                    //executorService.execute(() -> getToBuyGoodSkuInfoDetail(item, finalToBuyGoodInfo,skuKeyWords));
                                }
                            }
                        }/*else {
                            if(preToBuyGoodList.stream().noneMatch(good -> good.goodsId.equals(item.id))){
                                ToBuyGoodInfo toBuyGoodInfo = new ToBuyGoodInfo();
                                preToBuyGoodList.add(toBuyGoodInfo);
                                //realToBuyGoodList.add(toBuyGoodInfo);
                                if(i > 0){
                                    realToBuyGoodList.add(toBuyGoodInfo);
                                }
                                toBuyGoodInfo.goodsId = item.id;
                                toBuyGoodInfo.googsName = item.title;
                                toBuyGoodInfo.toBuyGoodSkuInfos = new ArrayList<>();

                                executorServiceForGoodDetail.execute(() -> getToBuyGoodSkuInfoDetail(item, toBuyGoodInfo,skuColorKeyWords,skuSizeKeyWords,skuStyleKeyWords,kdtSession,goodInfo.getToBuyNum()));

                            }
                        }*/
                    });

                }
            }
        });

        taskList.forEach(task -> {
            try {
                task.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        //System.out.println(i+" time to buildToBuyGoodInfo time cost:" + (System.currentTimeMillis() - startTime));
    }

    /**
     * 收件人信息
     * @param toBuyGoodAndAddressInfos
     */
    public static void buildToBuyAddressInfo(List<ToBuyGoodAndAddressInfo> toBuyGoodAndAddressInfos,String kdtSession, String kdtId) {
        AddressInfoDTO addressInfo = getAllAddressInfo(kdtSession, kdtId);
        //System.out.println(addressInfo);
        if(toBuyGoodAndAddressInfos != null && addressInfo != null && addressInfo.addressList != null){
            toBuyGoodAndAddressInfos.forEach(item -> {
                AddressDetailInfoDTO addressDetailInfo = addressInfo.addressList.stream().filter(address -> address.userName.contains(item.getReceiptName()) || address.addressDetail.contains(item.getReceiptName())).findFirst().orElse(null);
                if(addressDetailInfo != null){
                    item.setAddressDetailInfo(JSONObject.parseObject(JSONObject.toJSONString(addressDetailInfo),AddressDetailInfoDTO.class));
                }
            });
        }
    }

    /**
     * 移除已购商品，避免重复下单
     * 根据已购订单号存在即下单成功->所有待抢购商品都抢到
     * @param toBuyGoodAndAddressInfos
     * @param alreadyBuyGoodInfo
     * @param intendToBuyGoods
     */
    public static void removeAlreadyBuyAndToPayGood(List<ToBuyGoodAndAddressInfo> toBuyGoodAndAddressInfos, List<ToBuyGoodAndAddressInfo> alreadyBuyGoodInfo, List<ToBuyGoodAndAddressInfo> intendToBuyGoods) {
        if (toBuyGoodAndAddressInfos != null && !toBuyGoodAndAddressInfos.isEmpty()) {
            try {
                toBuyGoodAndAddressInfos.stream().filter(item -> item.getToBuyGoodInfoList() != null && !item.getToBuyGoodInfoList().isEmpty()).forEach(toBuy -> toBuy.setReadyToBuyGoodNum(toBuy.getToBuyGoodInfoList().stream().map(ToBuyGoodInfo::getToBuyGoodSkuInfos).mapToInt(item -> item.size()).sum()));
                alreadyBuyGoodInfo.addAll(toBuyGoodAndAddressInfos.stream().filter(item -> item.getCommitOrderInfoList() != null && !item.getCommitOrderInfoList().isEmpty() && item.getToBuyGoodInfoList() != null && !item.getToBuyGoodInfoList().isEmpty() && item.getCommitOrderInfoList().stream().filter(commitOrder -> commitOrder.getData() != null && commitOrder.getData().getOrderNo() != null && !commitOrder.getData().getOrderNo().isEmpty()).count() == item.getReadyToBuyGoodNum()).collect(Collectors.toList()));
                List<String> alreadyBuyLocalNos = alreadyBuyGoodInfo.stream().map(ToBuyGoodAndAddressInfo::getLocalNo).collect(Collectors.toList());
                toBuyGoodAndAddressInfos.removeIf(item -> item.getCommitOrderInfoList() != null && !item.getCommitOrderInfoList().isEmpty() && item.getToBuyGoodInfoList() != null && !item.getToBuyGoodInfoList().isEmpty() && item.getCommitOrderInfoList().stream().filter(commitOrder -> commitOrder.getData() != null && commitOrder.getData().getOrderNo() != null && !commitOrder.getData().getOrderNo().isEmpty()).count() >= item.getReadyToBuyGoodNum());
                //System.out.println("alreadyBuyLocalNos: " +alreadyBuyLocalNos);
                if(alreadyBuyLocalNos != null && !alreadyBuyLocalNos.isEmpty()){
                    WeChatBll.sendMessage("下单成功:"+intendToBuyGoods.stream().filter(intend -> alreadyBuyLocalNos.contains(intend.getLocalNo())).findFirst().orElse(new ToBuyGoodAndAddressInfo()).getDesc());
                    intendToBuyGoods.removeIf(item -> alreadyBuyLocalNos.contains(item.getLocalNo()));
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    /**
     * 取消待支付订单
     *  @param cancelOrderUrl
     * @param toPayStr
     * @param kdtId
     */
    public static void cancelToPayOrder(String cancelOrderUrl, String toPayStr, String kdtSession, String kdtId) {
        ToPayShopInfoDTO toPayShopInfo = JSONObject.parseObject(toPayStr, ToPayShopInfoDTO.class);
        if(toPayShopInfo != null && toPayShopInfo.data != null && toPayShopInfo.data.list != null){

            toPayShopInfo.data.list.forEach(item ->executorServiceForCancelOrder.execute(() -> RequestBll.doPost(cancelOrderUrl, RequestBll.getCancelOrderPostEntity(item.order_no,kdtId),kdtSession,kdtId)));
        }
    }

    /**
     * 构建商品明细
     * @param item
     * @param toBuyGoodInfo
     * @param skuColorKeyWords
     * @param skuSizeKeyWords
     * @param skuStyleKeyWords
     * @param skuSpecKeyWords
     * @param kdtSession
     * @param toBuyNum
     */
    public static void getToBuyGoodSkuInfoDetail(GoodInfo item, ToBuyGoodInfo toBuyGoodInfo, List<String> skuColorKeyWords, List<String> skuSizeKeyWords, List<String> skuStyleKeyWords, List<String> skuSpecKeyWords, String kdtSession, int toBuyNum, String kdtId, String shopId) {
        String goodData = RequestBll.doGet(MessageFormat.format(getGoodDataUrl,shopId,item.alias),kdtSession,kdtId);
        if(goodData == null || goodData.isEmpty()){
            return;
        }
        //System.out.println("stockData:"+ item.alias +":"+goodData);
        boolean hasStockData = false;
        if(goodData.indexOf("goods_data") > 0 && goodData.indexOf("if (_global.url") > 0){
            hasStockData = true;
            String stockData = EscapeUnicode.unescape(goodData.substring(goodData.indexOf("goods_data")+12,goodData.indexOf("if (_global.url")-8));
            //System.out.println("stockData:"+stockData);
            GoodStockData goodStockData = JSONObject.parseObject(stockData,GoodStockData.class);
            if(goodStockData != null && goodStockData.goods != null && goodStockData.sku != null){
                StringBuilder goodInfoSB = new StringBuilder("goodsId:"+item.id + "|goodsName:"+goodStockData.goods.title);// + "|"+goodStockData.goods.content);
                //部分商品无属性明细列表：颜色、尺寸、款式、规格
                if((goodStockData.sku.list == null || goodStockData.sku.list.isEmpty()) && goodStockData.sku.collectionId > 0){
                    boolean matchSku = true;
                    if(goodStockData.sku.stockNum > 0 && matchSku){
                        ToBuyGoodSkuInfo toBuyGoodSkuInfo = toBuyGoodInfo.toBuyGoodSkuInfos.stream().filter(info -> info.skuId.equals(goodStockData.sku.collectionId)).findFirst().orElse(null);
                        if(toBuyGoodSkuInfo == null){
                            System.out.println("toBuyNew collectionId");
                            toBuyGoodInfo.toBuyGoodSkuInfos.add(new ToBuyGoodSkuInfo(goodStockData.sku.collectionId,goodStockData.sku.stockNum < toBuyNum ? goodStockData.sku.stockNum:toBuyNum , ""));
                        }else {
                            System.out.println("toBuyUpdate collectionId");
                            toBuyGoodSkuInfo.num = goodStockData.sku.stockNum < toBuyNum ? goodStockData.sku.stockNum:toBuyNum;
                        }
                    }
                }else{
                    goodStockData.sku.list.forEach(skuItem -> {
                        goodInfoSB.append("|goodsSkuId:").append(skuItem.id).append("|goodsSkuInfo:").append(skuItem.sku).append("|goodsSkuInfostockNum:").append(skuItem.stockNum);
                        boolean isMatchSku = true;
                        if(skuItem.sku != null){
                            try {
                                skuItem.skuList = JSONObject.parseObject(skuItem.sku, new TypeReference<List<KV>>(){});
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                        if(skuItem.sku.contains("颜色") && skuColorKeyWords != null && !skuColorKeyWords.isEmpty()){
                            isMatchSku = isMatchSku && skuColorKeyWords.stream().anyMatch(keyword -> isSkuItemMatch(skuItem.sku, keyword));
                        }
                        if(skuItem.sku.contains("尺码") && skuSizeKeyWords != null && !skuSizeKeyWords.isEmpty()){
                            if(skuItem.skuList != null){
                                List<String> sizeList = skuItem.skuList.stream().filter(size -> size.getK().equalsIgnoreCase("尺码")).map(KV::getV).collect(Collectors.toList());
                                isMatchSku = isMatchSku && skuSizeKeyWords.stream().anyMatch(keyword -> keyword.equalsIgnoreCase(sizeList.get(0)));
                            }else {
                                isMatchSku = isMatchSku && skuSizeKeyWords.stream().anyMatch(keyword -> skuItem.sku.contains(keyword));
                            }
                        }
                        if(skuItem.sku.contains("款式") && skuStyleKeyWords != null && !skuStyleKeyWords.isEmpty()){
                            isMatchSku = isMatchSku && skuStyleKeyWords.stream().anyMatch(keyword -> isSkuItemMatch(skuItem.sku, keyword));
                        }
                        if(skuItem.sku.contains("规格") && skuSpecKeyWords != null && !skuSpecKeyWords.isEmpty()){
                            isMatchSku = isMatchSku && skuSpecKeyWords.stream().anyMatch(keyword -> isSkuItemMatch(skuItem.sku, keyword));
                        }
                        if(skuItem.stockNum > 0 && isMatchSku){
                            ToBuyGoodSkuInfo toBuyGoodSkuInfo = toBuyGoodInfo.toBuyGoodSkuInfos.stream().filter(info -> info.skuId.equals(skuItem.id)).findFirst().orElse(null);
                            if(toBuyGoodSkuInfo == null){
                                System.out.println("toBuyNew");
                                toBuyGoodInfo.toBuyGoodSkuInfos.add(new ToBuyGoodSkuInfo(skuItem.id,skuItem.stockNum < toBuyNum ? skuItem.stockNum:toBuyNum , skuItem.sku));
                            }else {
                                System.out.println("toBuyUpdate");
                                toBuyGoodSkuInfo.num = skuItem.stockNum < toBuyNum ? skuItem.stockNum:toBuyNum;
                            }
                        }
                    });
                }
            }
        }
        //第二种获取方式
        if(!hasStockData && goodData.indexOf("\"buyer_id\"") > 0 && goodData.indexOf("if (_global.url") > 0){
            String stockData = goodData.substring(goodData.indexOf("\"buyer_id\"")-1,goodData.indexOf("if (_global.url")-7);
            GoodStockDataV2 goodStockDataV2 = JSONObject.parseObject(stockData,GoodStockDataV2.class);
            if(goodStockDataV2 == null){
                return;
            }
            GoodStockDataV2Data  goodStockData = goodStockDataV2.goodsData;
            if(goodStockData != null && goodStockData.goods != null && goodStockData.skuInfo != null && !goodStockData.skuInfo.hideStock){
                if(goodStockData.skuInfo.skuStocks != null && !goodStockData.skuInfo.skuStocks.isEmpty()){
                    goodStockData.skuInfo.skuStocks.stream().filter(stock -> stock.stockNum > 0).forEach(stockItem -> {
                        boolean isMatchSku = true;
                        if(resetStockItem(stockItem,goodStockData.skuInfo)){
                            if(stockItem.skuPropList.stream().anyMatch(propItem -> propItem.getK().contains("颜色")) && skuColorKeyWords != null && !skuColorKeyWords.isEmpty()){
                                isMatchSku = isMatchSku && skuColorKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(stockItem.skuPropList.stream().filter(propItem -> propItem.getK().contains("颜色")), keyword, false));
                            }
                            if(stockItem.skuPropList.stream().anyMatch(propItem -> propItem.getK().contains("尺码")) && skuSizeKeyWords != null && !skuSizeKeyWords.isEmpty()){
                                isMatchSku = isMatchSku && skuSizeKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(stockItem.skuPropList.stream().filter(propItem -> propItem.getK().contains("尺码")), keyword, true));
                            }
                            if(stockItem.skuPropList.stream().anyMatch(propItem -> propItem.getK().contains("款式")) && skuStyleKeyWords != null && !skuStyleKeyWords.isEmpty()){
                                isMatchSku = isMatchSku && skuStyleKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(stockItem.skuPropList.stream().filter(propItem -> propItem.getK().contains("款式")), keyword, false));
                            }
                            if(stockItem.skuPropList.stream().anyMatch(propItem -> propItem.getK().contains("规格")) && skuSpecKeyWords != null && !skuSpecKeyWords.isEmpty()){
                                isMatchSku = isMatchSku && skuSpecKeyWords.stream().anyMatch(keyword -> isSkuItemListMatch(stockItem.skuPropList.stream().filter(propItem -> propItem.getK().contains("规格")), keyword, false));
                            }
                            if(isMatchSku){
                                ToBuyGoodSkuInfo toBuyGoodSkuInfo = toBuyGoodInfo.toBuyGoodSkuInfos.stream().filter(info -> info.skuId.equals(stockItem.skuId)).findFirst().orElse(null);
                                if(toBuyGoodSkuInfo == null){
                                    System.out.println("toBuyNew");
                                    toBuyGoodInfo.toBuyGoodSkuInfos.add(new ToBuyGoodSkuInfo(stockItem.skuId,stockItem.stockNum < toBuyNum ? stockItem.stockNum:toBuyNum , stockItem.toString()));
                                }else {
                                    System.out.println("toBuyUpdate");
                                    toBuyGoodSkuInfo.num = stockItem.stockNum < toBuyNum ? stockItem.stockNum:toBuyNum;
                                    if(toBuyGoodSkuInfo.num <= 0){
                                        toBuyGoodInfo.toBuyGoodSkuInfos.remove(toBuyGoodSkuInfo);
                                    }
                                }
                            }
                        }
                    });
                }else if(goodStockData.skuInfo.spuStock != null){
                    if(goodStockData.skuInfo.spuStock.stockNum > 0){
                        ToBuyGoodSkuInfo toBuyGoodSkuInfo = toBuyGoodInfo.toBuyGoodSkuInfos.stream().filter(info -> info.skuId.equals(goodStockData.skuInfo.spuStock.skuId)).findFirst().orElse(null);
                        if(toBuyGoodSkuInfo == null){
                            System.out.println("toBuyNew spuStock skuId");
                            toBuyGoodInfo.toBuyGoodSkuInfos.add(new ToBuyGoodSkuInfo(goodStockData.skuInfo.spuStock.skuId,goodStockData.skuInfo.spuStock.stockNum < toBuyNum ? goodStockData.skuInfo.spuStock.stockNum:toBuyNum , ""));
                        }else {
                            System.out.println("toBuyUpdate collectionId");
                            toBuyGoodSkuInfo.num = goodStockData.skuInfo.spuStock.stockNum < toBuyNum ? goodStockData.skuInfo.spuStock.stockNum:toBuyNum;
                        }
                    }
                }

            }
        }
    }

    private static boolean isSkuItemListMatch(Stream<KV> propList, String keyword, boolean strictEqual) {
        return propList.allMatch(item -> isSkuItemMatch(item,keyword,strictEqual));
    }

    private static boolean isSkuItemMatch(KV item, String keyword, boolean strictEqual) {
        if(strictEqual){
            return keyword.equalsIgnoreCase(item.getV());
        }
        return isSkuItemMatch(item.getV(),keyword);
    }

    private static boolean resetStockItem(SKUStockKVDTO stockItem, GoodStockSKUInfoV2 skuInfo) {
        if(skuInfo.skus == null || skuInfo.props == null){
            return false;
        }
        SKURelationDTO skuRelation = skuInfo.skus.stream().filter(item -> item.skuId == stockItem.skuId).findFirst().orElse(null);
        if(skuRelation == null){
            return false;
        }
        resetSKUProperty(stockItem,skuRelation,skuInfo.props);
        return true;
    }

    private static void resetSKUProperty(SKUStockKVDTO stockItem, SKURelationDTO skuRelation, List<SKUInfoPropDTO> props) {
        stockItem.skuPropList = new ArrayList<>();
        if(Integer.valueOf(skuRelation.s1) > 0){
            resetSKUPropertyDetail(stockItem,"s1",skuRelation.s1,props);
        }
        if(Integer.valueOf(skuRelation.s2) > 0){
            resetSKUPropertyDetail(stockItem,"s2",skuRelation.s2,props);
        }
        if(Integer.valueOf(skuRelation.s3) > 0){
            resetSKUPropertyDetail(stockItem,"s3",skuRelation.s3,props);
        }
        if(Integer.valueOf(skuRelation.s4) > 0){
            resetSKUPropertyDetail(stockItem,"s4",skuRelation.s4,props);
        }
        if(Integer.valueOf(skuRelation.s5) > 0){
            resetSKUPropertyDetail(stockItem,"s5",skuRelation.s5,props);
        }
    }

    private static void resetSKUPropertyDetail(SKUStockKVDTO stockItem, String propertyName,String propertyValue, List<SKUInfoPropDTO> props) {
        SKUInfoPropDTO skuInfoProp = props.stream().filter(item -> item.k_s.equalsIgnoreCase(propertyName)).findFirst().orElse(null);
        if(skuInfoProp != null && skuInfoProp.v != null){
            IdNameKVDTO idNameKV = skuInfoProp.v.stream().filter(item -> Integer.valueOf(propertyValue) == item.id).findFirst().orElse(null);
            if(idNameKV != null){
                stockItem.skuPropList.add(new KV(skuInfoProp.k,idNameKV.name));
            }
        }
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
     * 获取地址信息
     * @return
     */
    public static AddressInfoDTO getAllAddressInfo(String kdtSession, String kdtId){
        AddressInfoDTO addressInfo = null;
        String addressInfoStr = RequestBll.doGet(getAddressInfoUrl,kdtSession,kdtId);
        String addressDetail = null;
        if(addressInfoStr != null && addressInfoStr.indexOf("buyer_id") > 0 && addressInfoStr.indexOf("if (_global.url") > 0){
            addressDetail = addressInfoStr.substring(addressInfoStr.indexOf("buyer_id") -2, addressInfoStr.indexOf("if (_global.url"));
        }
        try {
            addressInfo = JSONObject.parseObject(addressDetail, AddressInfoDTO.class);
            if(addressInfo != null && addressInfo.addressList != null){
                addressInfo.addressList.forEach(address -> address.addressDetail = address.addressDetail.replace("\n"," "));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return addressInfo;
    }

    /**
     * 获取待支付单个订单收件人地址信息
     * @param kdtSession
     * @param kdtId
     * @param orderNo
     * @return
     */
    public static ToPaySingleOrderInfo getToPayOrderAddressInfo(String kdtSession,String kdtId, String orderNo){
        ToPaySingleOrderInfo toPaySingleOrderInfo = null;
        try {
            String addressInfoStr = RequestBll.doGet(MessageFormat.format(getTopayGoodDetailUrl,orderNo,kdtId),kdtSession,kdtId);
            if(addressInfoStr != null && addressInfoStr.indexOf("buyer_id") > 0 && addressInfoStr.indexOf("if (_global.url") > 0){
                addressInfoStr = addressInfoStr.substring(addressInfoStr.indexOf("buyer_id") -2, addressInfoStr.indexOf("if (_global.url"));
            }
            if(addressInfoStr != null && !addressInfoStr.isEmpty()){
                toPaySingleOrderInfo = JSONObject.parseObject(addressInfoStr, ToPaySingleOrderInfo.class);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return toPaySingleOrderInfo;
    }

}
