package com.myqq.service.youza.bll;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.entity.IntendOrderDTO;
import com.myqq.service.youza.entity.ToPayOrderDTO;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.myqq.service.youza.bll.ConstInfo.*;
import static com.myqq.service.youza.bll.ShopInfoDTO.*;
import static com.myqq.service.youza.bll.RequestBll.*;
import static com.myqq.service.youza.bll.ShoppingBll.*;

@Service
public class AutoShoppingEntry {

    /**
     * kdtSessionId-List
     */
    public static ConcurrentHashMap<String,List<ToBuyGoodAndAddressInfo>> mapIntendToBuyGoodInfos = new ConcurrentHashMap(15);
    public static ConcurrentHashMap<String,List<ToBuyGoodAndAddressInfo>> mapToBuyGoodAndAddressInfos = new ConcurrentHashMap(15);
    public static ConcurrentHashMap<String,List<ToBuyGoodAndAddressInfo>> mapAlreadyBuyGoodAndAddressInfos = new ConcurrentHashMap(15);
    public static ConcurrentHashMap<String,Boolean> mapStartShoppingSymbol = new ConcurrentHashMap<>(2);
    public static ConcurrentHashMap<String,List<AddressDetailInfoDTO>> mapAddressInfos= new ConcurrentHashMap<>(2);

    public static boolean testConnect(String kdtSessionId,String kdtId){
        initMapInfoBySessionNO(kdtSessionId);
        String addressInfo = doGet(getAddressInfoUrl, kdtSessionId, kdtId);
        if(addressInfo != null && addressInfo.contains("请在微信客户端打开链接")){
            System.out.println(addressInfo);
            return false;
        }
        return true;
    }

    static void initMapInfoBySessionNO(String kdtSessionId){
        if(mapIntendToBuyGoodInfos.get(kdtSessionId) == null){
            mapIntendToBuyGoodInfos.putIfAbsent(kdtSessionId, new ArrayList<>());
            mapIntendToBuyGoodInfos.remove("",null);
        }
        if(mapToBuyGoodAndAddressInfos.get(kdtSessionId) == null){
            mapToBuyGoodAndAddressInfos.putIfAbsent(kdtSessionId, new ArrayList<>());
        }
        if(mapAlreadyBuyGoodAndAddressInfos.get(kdtSessionId) == null){
            mapAlreadyBuyGoodAndAddressInfos.putIfAbsent(kdtSessionId, new ArrayList<>());
        }
        if(mapStartShoppingSymbol.get(kdtSessionId) == null){
            mapStartShoppingSymbol.putIfAbsent(kdtSessionId,false);
        }
        if(mapAddressInfos.get(kdtSessionId) == null){
            mapAddressInfos.putIfAbsent(kdtSessionId, new ArrayList<>());
        }
    }


    //region  jobtest
    /*public static void startOrderJob(String kdtSession){
        StringBuilder result = new StringBuilder(Thread.currentThread().getName()+"startOrderJob");
        System.out.println(Thread.currentThread().getName()+" startOrderJob");
        testConnect(kdtSession);
        //无抢单任务时  每隔五分钟检测一次
        for(;;){
            for(long i=0;;i++){
                initMapInfoBySessionNO(kdtSession);
                StringBuilder resultBuilder = new StringBuilder("第 ").append(i).append(" 次抢单");
                System.out.println(resultBuilder.toString());
                buildToBuyGoodInfo(i,mapToBuyGoodAndAddressInfos.get(kdtSession),kdtSession);
                commitToBuyOrder(buyUrl,mapToBuyGoodAndAddressInfos.get(kdtSession),kdtSession);
                removeAlreadyBuyAndToPayGood(mapToBuyGoodAndAddressInfos.get(kdtSession),mapAlreadyBuyGoodAndAddressInfos.get(kdtSession),mapIntendToBuyGoodInfos.get(kdtSession));
                if(mapToBuyGoodAndAddressInfos.isEmpty() || mapToBuyGoodAndAddressInfos.get(kdtSession).isEmpty() || mapToBuyGoodAndAddressInfos.get(kdtSession).size() == mapAlreadyBuyGoodAndAddressInfos.get(kdtSession).size()){
                    resultBuilder.append("抢购完成:").append(MessageFormat.format("{0}个订单",mapAlreadyBuyGoodAndAddressInfos.get(kdtSession).size()));
                    System.out.println(resultBuilder.toString());
                }
            }
        }
    }*/
    //endregion

    /**
     * 根据任务自动下单
     *
     * @param localNos
     * @param kdtSession
     * @return
     */
    public static String startAutoShopping(String kdtSession ,String localNos, String kdtId,String shopId, String goodsTagId){
        initMapInfoBySessionNO(kdtSession);
        StringBuilder resultBuilder = new StringBuilder();
        String standToBuyLocalNos = mapToBuyGoodAndAddressInfos.get(kdtSession).stream().map(ToBuyGoodAndAddressInfo::getLocalNo).collect(Collectors.joining(","));
        if(mapIntendToBuyGoodInfos.get(kdtSession).size() > 0){
            List<ToBuyGoodAndAddressInfo> needToAddIntends = mapIntendToBuyGoodInfos.get(kdtSession).stream().filter(item -> localNos.contains(item.getLocalNo()) && !standToBuyLocalNos.contains(item.getLocalNo())).collect(Collectors.toList());
            if(needToAddIntends != null && !needToAddIntends.isEmpty()){
                mapToBuyGoodAndAddressInfos.get(kdtSession).addAll(needToAddIntends);
            }else {
                return "任务已添加";
            }
        }else {
            return "无意向单";
        }
        //buildToBuyAddressInfo(mapToBuyGoodAndAddressInfos.get(kdtSession),kdtSession,kdtId);
        if(mapStartShoppingSymbol.get(kdtSession)){
            return "抢单中";
        }
        for(long i=0;;i++){
            mapStartShoppingSymbol.put(kdtSession,true);
            //System.out.println(resultBuilder.toString());
            try {
                buildToBuyGoodInfo(i,mapToBuyGoodAndAddressInfos.get(kdtSession),kdtSession,kdtId,shopId,goodsTagId);
                //System.out.println(Calendar.getInstance().toInstant().toString() +" "+ i +" times buildToBuyGoodInfo mapToBuyGoodAndAddressInfos:"+ JSONObject.toJSONString(mapToBuyGoodAndAddressInfos.get(kdtSession)));
                //System.out.println(Calendar.getInstance().toInstant().toString() +" "+ i +" times buildToBuyGoodInfo mapToBuyGoodAndAddressInfos:"+ JSONObject.toJSONString(mapToBuyGoodAndAddressInfos.get(kdtSession)));
                commitToBuyOrder(buyUrl,mapToBuyGoodAndAddressInfos.get(kdtSession),kdtSession,kdtId);
                removeAlreadyBuyAndToPayGood(mapToBuyGoodAndAddressInfos.get(kdtSession),mapAlreadyBuyGoodAndAddressInfos.get(kdtSession),mapIntendToBuyGoodInfos.get(kdtSession));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            if(mapToBuyGoodAndAddressInfos.get(kdtSession).isEmpty()){
                resultBuilder.append("抢购完成:").append(MessageFormat.format("{0}个订单,详情：{1}",mapAlreadyBuyGoodAndAddressInfos.get(kdtSession).size(),JSONObject.toJSONString(mapAlreadyBuyGoodAndAddressInfos.get(kdtSession))));
                System.out.println(resultBuilder.toString());
                mapStartShoppingSymbol.put(kdtSession,false);
                return resultBuilder.toString();
            }
        }
    }

    /**
     * 取消待支付订单
     *
     * @param orderNoList
     * @return
     */
    public static String cancelOrder(List<String> orderNoList,String kdtSession,String kdtId){
        initMapInfoBySessionNO(kdtSession);
        String toPayStr = doGet(getTopayListUrl,kdtSession, kdtId);
        cancelToPayOrder(cancelOrderUrl,toPayStr,kdtSession,kdtId);
        return "";
    }

    /**
     * 抢单示例
     * @param intendOrderInfo
     * @param kdtSession
     * @return
     */
    public static List<ToBuyGoodAndAddressInfo> getToBuyGoodAndAddressInfoList(IntendOrderDTO intendOrderInfo , String kdtSession){
        initMapInfoBySessionNO(kdtSession);
        if(intendOrderInfo != null && intendOrderInfo.getReceptNameList() != null && !intendOrderInfo.getReceptNameList().isEmpty()){
            List<String> receptNames = Arrays.asList(intendOrderInfo.getReceptNameList().split("\\,"));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            receptNames.forEach(item -> {
                ToBuyGoodAndAddressInfo tmpToBuy = new ToBuyGoodAndAddressInfo();
                mapIntendToBuyGoodInfos.get(kdtSession).add(tmpToBuy);
                //intendToBuyGoods.add(tmpToBuy);
                tmpToBuy.setOrderDate(dateTimeFormatter.format(LocalDateTime.now()));
                tmpToBuy.setLocalNo(UUID.randomUUID().toString());
                tmpToBuy.setKdtSession(kdtSession);
                tmpToBuy.setShotGoodName(intendOrderInfo.getGoodShotName());

                tmpToBuy.setToBuyNum(intendOrderInfo.getGoodNum());
                tmpToBuy.setNeedPresale(intendOrderInfo.isNeedPresale());
                StringBuilder descSB = new StringBuilder("名称:");
                descSB.append(intendOrderInfo.getGoodShotName());
                if(mapAddressInfos.get(kdtSession) != null){
                    AddressDetailInfoDTO addressDetailInfo = mapAddressInfos.get(kdtSession).stream().filter(address -> address.id == Integer.valueOf(item)).findFirst().orElse(null);
                    if(addressDetailInfo != null){
                        tmpToBuy.setAddressDetailInfo(addressDetailInfo);
                        tmpToBuy.setReceiptName(addressDetailInfo.userName);
                        descSB.append(" 收件人:").append(addressDetailInfo.userName);
                    }else {

                    }
                }

                descSB.append(" 数量:").append(intendOrderInfo.getGoodNum());
                if(intendOrderInfo.getGoodColorList() != null && !intendOrderInfo.getGoodColorList().isEmpty()){
                    tmpToBuy.setGoodColorKeyWords(Arrays.asList(intendOrderInfo.getGoodColorList().split("\\,")));
                    descSB.append(" 颜色:").append(intendOrderInfo.getGoodColorList());
                }
                if(intendOrderInfo.getGoodSizeList() != null && !intendOrderInfo.getGoodSizeList().isEmpty()){
                    tmpToBuy.setGoodSizeKeyWords(Arrays.asList(intendOrderInfo.getGoodSizeList().split("\\,")));
                    descSB.append(" 尺码:").append(intendOrderInfo.getGoodSizeList());
                }
                if(intendOrderInfo.getGoodStyleList() != null && !intendOrderInfo.getGoodStyleList().isEmpty()){
                    tmpToBuy.setGoodStyleKeyWords(Arrays.asList(intendOrderInfo.getGoodStyleList().split("\\,")));
                    descSB.append(" 款式:").append(intendOrderInfo.getGoodStyleList());
                }
                if(intendOrderInfo.getGoodSpecList() != null && !intendOrderInfo.getGoodSpecList().isEmpty()){
                    tmpToBuy.setGoodSpecKeyWords(Arrays.asList(intendOrderInfo.getGoodSpecList().split("\\,")));
                    descSB.append(" 规格:").append(intendOrderInfo.getGoodSpecList());
                }
                descSB.append(" 预售:").append(intendOrderInfo.isNeedPresale() ? "是":"否").append(" 时间:").append(tmpToBuy.getOrderDate());
                tmpToBuy.setDesc(descSB.toString());
            });
        }
        return mapIntendToBuyGoodInfos.get(kdtSession);
    }

    public static String getAddressInfo(String kdtSession,String kdtId){
        initMapInfoBySessionNO(kdtSession);
        AddressInfoDTO addressInfo = getAllAddressInfo(kdtSession,kdtId);
        if(addressInfo != null && addressInfo.addressList != null){
            List<AddressDetailInfoDTO> addressDetailInfoDTOS = mapAddressInfos.get(kdtSession);
            if(addressDetailInfoDTOS == null){
                addressDetailInfoDTOS = new ArrayList<>();
            }
            List<AddressDetailInfoDTO> finalAddressDetailInfoDTOS = addressDetailInfoDTOS;
            addressInfo.addressList.forEach(item -> {
                if(finalAddressDetailInfoDTOS.stream().noneMatch(address -> address.id == item.id)){
                    finalAddressDetailInfoDTOS.add(item);
                }
            });
        }
        return JSONObject.toJSONString(addressInfo);
    }

    public static boolean removeFromIntendOrder(String kdtSession, String localNo){

        return mapIntendToBuyGoodInfos.get(kdtSession).removeIf(item -> item.getLocalNo().equalsIgnoreCase(localNo));
    }

    public static boolean clearAllOrderInfo(String kdtSession) {
        initMapInfoBySessionNO(kdtSession);
        mapToBuyGoodAndAddressInfos.get(kdtSession).clear();
        mapAlreadyBuyGoodAndAddressInfos.get(kdtSession).clear();
        mapIntendToBuyGoodInfos.get(kdtSession).clear();
        return true;
    }
    public static boolean clearStandToBuyOrderInfo(String kdtSession) {
        initMapInfoBySessionNO(kdtSession);
        mapToBuyGoodAndAddressInfos.get(kdtSession).clear();
        return true;
    }

    public static List<ToPayOrderDTO> getToPayOrderList(String kdtSession,String kdtId) {
        initMapInfoBySessionNO(kdtSession);
        List<ToPayOrderDTO> toPayOrderDTOS = new ArrayList<>();
        StandToPayData toPayData = JSONObject.parseObject(doGet(getTopayListUrl,kdtSession,kdtId),StandToPayData.class);
        if(toPayData != null && toPayData.getData() != null && toPayData.getData().getList() != null && !toPayData.getData().getList().isEmpty()){
            toPayData.getData().getList().forEach(item -> {
                ToPayOrderDTO toPayOrderDTO = new ToPayOrderDTO();
                toPayOrderDTOS.add(toPayOrderDTO);
                toPayOrderDTO.setOrderNo(item.getOrder_items().get(0).getOrder_no());
                try {
                    ToBuyGoodAndAddressInfo alreadyBuyInfo = mapAlreadyBuyGoodAndAddressInfos.get(kdtSession).stream().filter(alreadyBuyGoodInfo -> alreadyBuyGoodInfo.getCommitOrderInfoList() != null && alreadyBuyGoodInfo.getCommitOrderInfoList().stream().anyMatch(commit -> commit.getData().getOrderNo().equalsIgnoreCase(item.getOrder_items().get(0).getOrder_no()))).findFirst().orElse(null);
                    if(alreadyBuyInfo != null){
                        toPayOrderDTO.setReceipt(alreadyBuyInfo.getReceiptName());
                    }else {
                        ToPaySingleOrderInfo toPaySingleOrderInfo = getToPayOrderAddressInfo(kdtSession,kdtId,toPayOrderDTO.getOrderNo());
                        if(toPaySingleOrderInfo != null && toPaySingleOrderInfo.getOrderAddressInfo() != null){
                            toPayOrderDTO.setReceipt(toPaySingleOrderInfo.getOrderAddressInfo().getReceiverName());
                        }
                    }
                }catch (Exception ex){
                    toPayOrderDTO.setReceipt(ex.getMessage());
                    ex.printStackTrace();
                }

                StringBuilder attr = new StringBuilder();
                attr.append(toPayOrderDTO.getOrderNo()).append(",收件人:").append(toPayOrderDTO.getReceipt()).append(";名称:").append(item.getOrder_items().get(0).getItems().get(0).getTitle()).append(";价格:").append(item.getOrder_items().get(0).getTotal_price());
                item.getOrder_items().get(0).getItems().get(0).getSku().forEach(detail -> attr.append(detail.getK()).append(":").append(detail.getV()).append(";"));
                toPayOrderDTO.setImgUrl(item.getOrder_items().get(0).getItems().get(0).getImage());
                toPayOrderDTO.setDesc(attr.toString());

            });
        }

        return toPayOrderDTOS;
    }

    public static List<ToPayOrderDTO> getToSendOrderList(String kdtSession,String kdtId) {
        initMapInfoBySessionNO(kdtSession);
        List<ToPayOrderDTO> toPayOrderDTOS = new ArrayList<>();
        StandToPayData toPayData = JSONObject.parseObject(doGet(getToSendGoodUrl,kdtSession,kdtId),StandToPayData.class);
        if(toPayData != null && toPayData.getData() != null && toPayData.getData().getList() != null){
            toPayData.getData().getList().forEach(item -> {
                ToPayOrderDTO toPayOrderDTO = new ToPayOrderDTO();
                toPayOrderDTOS.add(toPayOrderDTO);
                try{
                    ToBuyGoodAndAddressInfo alreadyBuyInfo = mapAlreadyBuyGoodAndAddressInfos.get(kdtSession).stream().filter(alreadyBuyGoodInfo -> alreadyBuyGoodInfo.getCommitOrderInfoList() != null && alreadyBuyGoodInfo.getCommitOrderInfoList().stream().anyMatch(commit -> commit.getData().getOrderNo().equalsIgnoreCase(item.getOrder_items().get(0).getOrder_no()))).findFirst().orElse(null);
                    if(alreadyBuyInfo != null){
                        toPayOrderDTO.setReceipt(alreadyBuyInfo.getReceiptName());
                    }
                }catch (Exception ex){
                    toPayOrderDTO.setReceipt(ex.getMessage());
                    ex.printStackTrace();
                }

                toPayOrderDTO.setOrderNo(item.getOrder_items().get(0).getOrder_no());
                StringBuilder attr = new StringBuilder();
                attr.append(toPayOrderDTO.getOrderNo()).append(",收件人:").append(toPayOrderDTO.getReceipt()).append(";价格:").append(item.getOrder_items().get(0).getTotal_price());
                item.getOrder_items().get(0).getItems().get(0).getSku().forEach(detail -> attr.append(detail.getK()).append(":").append(detail.getV()).append(";"));
                toPayOrderDTO.setImgUrl(item.getOrder_items().get(0).getItems().get(0).getImage());
                toPayOrderDTO.setDesc(attr.toString());

            });
        }

        return toPayOrderDTOS;
    }

    /**
     * delete order info from intend order list and stand to buy order list
     * @param kdtSession
     * @param localNos
     * @return
     */
    public static boolean deleteIntendOrders(String kdtSession, String localNos) {
        initMapInfoBySessionNO(kdtSession);
        mapIntendToBuyGoodInfos.get(kdtSession).removeIf(item -> localNos.contains(item.getLocalNo()));
        mapToBuyGoodAndAddressInfos.get(kdtSession).removeIf(item -> localNos.contains(item.getLocalNo()));
        return true;
    }

    /**
     * kdtid|shopid|goodslisttagid
     * @param shopInfo
     * @param index
     * @return
     */
    public static String getIdByIndex(String shopInfo, int index) {
        if(shopInfo == null){
            return "";
        }
        return shopInfo.split("\\|")[index];
    }

    /**
     * 订单号非法？？
     * @param kdtSession
     * @param kdtId
     * @return
     */
    public static List<ToPayOrderDTO> getSendedOrderList(String kdtSession, String kdtId) {
        initMapInfoBySessionNO(kdtSession);
        List<ToPayOrderDTO> toPayOrderDTOS = new ArrayList<>();
        StandToPayData toPayData = JSONObject.parseObject(doGet(getSendGoodUrl,kdtSession,kdtId),StandToPayData.class);
        if(toPayData != null && toPayData.getData() != null && toPayData.getData().getList() != null){
            toPayData.getData().getList().forEach(item -> {
                ToPayOrderDTO toPayOrderDTO = new ToPayOrderDTO();
                toPayOrderDTOS.add(toPayOrderDTO);
                try{
                    ToBuyGoodAndAddressInfo alreadyBuyInfo = mapAlreadyBuyGoodAndAddressInfos.get(kdtSession).stream().filter(alreadyBuyGoodInfo -> alreadyBuyGoodInfo.getCommitOrderInfoList() != null && alreadyBuyGoodInfo.getCommitOrderInfoList().stream().anyMatch(commit -> commit.getData().getOrderNo().equalsIgnoreCase(item.getOrder_items().get(0).getOrder_no()))).findFirst().orElse(null);
                    if(alreadyBuyInfo != null){
                        toPayOrderDTO.setReceipt(alreadyBuyInfo.getReceiptName());
                    }else {
                        ToPaySingleOrderInfo toPaySingleOrderInfo = getToPayOrderAddressInfo(kdtSession,kdtId,toPayOrderDTO.getOrderNo());
                        if(toPaySingleOrderInfo != null && toPaySingleOrderInfo.getOrderAddressInfo() != null){
                            toPayOrderDTO.setReceipt(toPaySingleOrderInfo.getOrderAddressInfo().getReceiverName());
                        }
                    }
                }catch (Exception ex){
                    toPayOrderDTO.setReceipt(ex.getMessage());
                    ex.printStackTrace();
                }

                toPayOrderDTO.setOrderNo(item.getOrder_items().get(0).getOrder_no());
                StringBuilder attr = new StringBuilder();
                attr.append(toPayOrderDTO.getOrderNo()).append(",收件人:").append(toPayOrderDTO.getReceipt()).append(";价格:").append(item.getOrder_items().get(0).getTotal_price());
                item.getOrder_items().get(0).getItems().get(0).getSku().forEach(detail -> attr.append(detail.getK()).append(":").append(detail.getV()).append(";"));
                toPayOrderDTO.setImgUrl(item.getOrder_items().get(0).getItems().get(0).getImage());
                toPayOrderDTO.setDesc(attr.toString());

            });
        }

        return toPayOrderDTOS;
    }
}
