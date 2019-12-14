package com.myqq.service.youza.bll;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.entity.IntendOrderDTO;
import com.myqq.service.youza.entity.ShoppingForAppDTO;
import com.myqq.service.youza.entity.ToBuyGoodInfoAppDTO;
import com.myqq.service.youza.entity.ToPayOrderDTO;

import java.io.*;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.myqq.service.youza.bll.ShoppingBll.getAllAddressInfo;
import static com.myqq.service.youza.constinfo.ConstInfoForApp.*;

public class AutoShoppingEntryForApp {

    //region auth-List 订单信息
    /**
     * 意向单
     */
    public static ConcurrentHashMap<String, List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO>> mapIntendToBuyGoodInfos = new ConcurrentHashMap(15);
    /**
     * 下单中
     */
    public static ConcurrentHashMap<String,List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO>> mapToBuyGoodAndAddressInfos = new ConcurrentHashMap(15);
    /**
     * 已下单
     */
    public static ConcurrentHashMap<String,List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO>> mapAlreadyBuyGoodAndAddressInfos = new ConcurrentHashMap(15);
    /**
     * 是否开始抢购标识
     */
    public static ConcurrentHashMap<String,Boolean> mapStartShoppingSymbol = new ConcurrentHashMap<>(2);
    /**
     * 收件人信息
     */
    public static ConcurrentHashMap<String,List<ShoppingForAppDTO.AddressDataRowDetailDTO>> mapAddressInfos= new ConcurrentHashMap<>(2);
    //endregion
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ");
    /**
     * 链接测试
     *
     * @param auth
     * @param memberId
     * @return
     */
    public static boolean testConnect(String auth, String memberId) {
        initMapInfoByAuth(auth);
        String addressInfo = RequestBllForApp.doGet(MessageFormat.format(getAddressInfoUrlForApp, memberId), auth);
        try {
            if(addressInfo != null){
                ShoppingForAppDTO.AddressDataDTO addressData = JSONObject.parseObject(addressInfo,ShoppingForAppDTO.AddressDataDTO.class);
                return addressData != null && addressData.getMessage().equals("成功");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 用户下单信息初始化--用auth初始化是为了多用户使用
     *
     * @param auth
     */
    private static void initMapInfoByAuth(String auth) {
        if(mapIntendToBuyGoodInfos.get(auth) == null){
            mapIntendToBuyGoodInfos.putIfAbsent(auth, new ArrayList<>());
            mapIntendToBuyGoodInfos.remove("",null);
        }
        if(mapToBuyGoodAndAddressInfos.get(auth) == null){
            mapToBuyGoodAndAddressInfos.putIfAbsent(auth, new ArrayList<>());
        }
        if(mapAlreadyBuyGoodAndAddressInfos.get(auth) == null){
            mapAlreadyBuyGoodAndAddressInfos.putIfAbsent(auth, new ArrayList<>());
        }
        if(mapStartShoppingSymbol.get(auth) == null){
            mapStartShoppingSymbol.putIfAbsent(auth,false);
        }
        if(mapAddressInfos.get(auth) == null){
            mapAddressInfos.putIfAbsent(auth, new ArrayList<>());
        }
    }

    /**
     * 获取收件人地址信息
     *
     * @param auth
     * @param memberId
     * @return
     */
    public static String getAddressInfo(String auth, String memberId, boolean useLocalAddress) {
        initMapInfoByAuth(auth);
        String addressInfo = RequestBllForApp.doGet(MessageFormat.format(getAddressInfoUrlForApp, memberId), auth);
        try {
            if(addressInfo != null){
                ShoppingForAppDTO.AddressDataDTO addressData = JSONObject.parseObject(addressInfo,ShoppingForAppDTO.AddressDataDTO.class);
                if(addressData != null && addressData.getMessage().equals("成功") && addressData.getData() != null){
                    List<ShoppingForAppDTO.AddressDataRowDetailDTO> addressDetailInfoDTOS = mapAddressInfos.get(auth);
                    if(addressDetailInfoDTOS == null){
                        addressDetailInfoDTOS = new ArrayList<>();
                    }
                    List<ShoppingForAppDTO.AddressDataRowDetailDTO> finalAddressDetailInfoDTOS = addressDetailInfoDTOS;
                    addressData.getData().getRows().forEach(item -> {
                        if(finalAddressDetailInfoDTOS.stream().noneMatch(address -> address.getAddressId() != null && address.getAddressId().equalsIgnoreCase(item.getAddressId()))){
                            finalAddressDetailInfoDTOS.add(item);
                        }
                    });

                    String addressInfoJsonStr = JSONObject.toJSONString(addressData.getData().getRows());
                    if(useLocalAddress){
                        return syncLocalAddress(addressInfoJsonStr, memberId, addressDetailInfoDTOS);
                    }

                    return addressInfoJsonStr;
                }
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return "";
        }
        return "";
    }

    public static String syncLocalAddress(String remoteAddress, String memberId, List<ShoppingForAppDTO.AddressDataRowDetailDTO> addressDetailInfoDTOS){
        String path = "addressinfo" + memberId + ".txt";
        File file = new File(path);
        if(file.exists()){
            System.out.println(path + " exist");
        }else {
            System.out.println(path + " not exist");
        }
        StringBuilder existAddress = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                System.out.println(line);
                existAddress.append(line);
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
        List<ShoppingForAppDTO.AddressDataRowDetailDTO> toAddAddress = new ArrayList<>();
        List<ShoppingForAppDTO.AddressDataRowDetailDTO> localAddressList = new ArrayList<>();
        if(existAddress.toString().length() > 0){
            localAddressList = JSONObject.parseArray(existAddress.toString(),ShoppingForAppDTO.AddressDataRowDetailDTO.class);
        }
        //String address = RequestBllForApp.doGet(MessageFormat.format(getAddressInfoUrlForApp, zzjjMemberId), zzjjAuth);
        try {
            if(remoteAddress != null && remoteAddress.trim() != "null" && remoteAddress.trim() != ""){
                List<ShoppingForAppDTO.AddressDataRowDetailDTO> remoteAddressList = JSONObject.parseArray(remoteAddress,ShoppingForAppDTO.AddressDataRowDetailDTO.class );
                if(remoteAddressList != null && !remoteAddressList.isEmpty()){
                    List<ShoppingForAppDTO.AddressDataRowDetailDTO> finalLocalAddressList = localAddressList;
                    remoteAddressList.forEach(item -> {
                        ShoppingForAppDTO.AddressDataRowDetailDTO tmpInfo = new ShoppingForAppDTO.AddressDataRowDetailDTO();
                        tmpInfo.setAddressId(item.getAddressId());
                        tmpInfo.setReceiveName(item.getReceiveName());
                        tmpInfo.setReceivePhone(item.getReceivePhone());
                        tmpInfo.setProvince(item.getProvince());
                        tmpInfo.setCity(item.getCity());
                        tmpInfo.setArea(item.getArea());
                        tmpInfo.setAddress(item.getAddress());
                        if(!finalLocalAddressList.contains(tmpInfo)){
                            toAddAddress.add(tmpInfo);
                            finalLocalAddressList.add(tmpInfo);
                        }
                    });
                }
            }
            if(!toAddAddress.isEmpty()){
                System.out.println("toAddAddress count: "+ toAddAddress.size());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(JSONObject.toJSONString(localAddressList).getBytes());
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
        StringBuilder addressInfoSb = new StringBuilder();
        String addressInfo = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ((addressInfo = bufferedReader.readLine()) != null){
                addressInfoSb.append(addressInfo);
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            localAddressList.forEach(address -> {
                if(!addressDetailInfoDTOS.contains(address)){
                    addressDetailInfoDTOS.add(address);
                }
            });
            return JSONObject.toJSONString(addressDetailInfoDTOS);
        }
    }

    /**
     * 删除本地意向单
     *
     * @param auth
     * @param localNos
     * @return
     */
    public static boolean deleteIntendOrders(String auth, String localNos) {
        initMapInfoByAuth(auth);
        mapIntendToBuyGoodInfos.get(auth).removeIf(item -> localNos.contains(item.getLocalNo()));
        mapToBuyGoodAndAddressInfos.get(auth).removeIf(item -> localNos.contains(item.getLocalNo()));
        return true;
    }

    /**
     * 清空记录
     *
     * @param auth
     * @return
     */
    public static boolean clearAllOrderInfo(String auth) {
        initMapInfoByAuth(auth);
        mapToBuyGoodAndAddressInfos.get(auth).clear();
        mapAlreadyBuyGoodAndAddressInfos.get(auth).clear();
        mapIntendToBuyGoodInfos.get(auth).clear();
        return true;
    }

    /**
     * 清除下单中订单
     *
     * @param auth
     * @return
     */
   public static boolean clearStandToBuyOrderInfo(String auth) {
        initMapInfoByAuth(auth);
        mapToBuyGoodAndAddressInfos.get(auth).clear();
        return true;
    }

    /**
     * 取消待支付订单
     *
     * @param orderNos
     * @param auth
     * @param memberId
     * @return
     */
    public static String cancelOrder(String orderNos, String auth, String memberId) {
        AtomicReference<String> result = new AtomicReference<>("success");
        List<String> orderList = Arrays.asList(orderNos.split("\\,"));
        try {
            if(orderList.stream().filter(item -> item != null && !item.trim().isEmpty()).count() == 0){
                List<ToPayOrderDTO> toPayOrderDTOS = getToPayOrderList(auth, memberId, getToPayGoodDetailUrlForApp);
                orderList = toPayOrderDTOS.stream().map(ToPayOrderDTO::getOrderNo).collect(Collectors.toList());
            }
            orderList.stream().filter(item -> item != null && !item.trim().isEmpty()).forEach(orderNo -> {
                String reqResultInfo = RequestBllForApp.doPatch(MessageFormat.format(cancelOrderUrlForApp, orderNo), auth);
                if(reqResultInfo != null){
                    ShoppingForAppDTO.CommitOrderDTO commitOrder = JSONObject.parseObject(reqResultInfo, ShoppingForAppDTO.CommitOrderDTO.class);
                    if(commitOrder != null && !commitOrder.getMessage().equals("成功")){
                        result.set(result.get() + orderNo + commitOrder.getMessage());
                    }
                }
            });
        }catch (Exception ex){
            result.set(ex.getMessage());
        }
        return result.get();
    }

    /**
     * 获取待支付订单
     *
     * @param auth
     * @param memberId
     * @return
     */
    public static List<ToPayOrderDTO> getToPayOrderList(String auth, String memberId, String dataUrl) {
        List<ToPayOrderDTO> toPayOrderDTOS = new ArrayList<>();
        initMapInfoByAuth(auth);
        String toPayInfo = RequestBllForApp.doGet(MessageFormat.format(dataUrl, memberId), auth);
        try {
            if(toPayInfo != null){
                ShoppingForAppDTO.OrderDataDTO toPayData = JSONObject.parseObject(toPayInfo, ShoppingForAppDTO.OrderDataDTO.class);
                if(toPayData != null && toPayData.getMessage().equals("成功") && toPayData.getData() != null && toPayData.getData().getRows() != null){
                    toPayData.getData().getRows().forEach(toPayItem -> {
                        ToPayOrderDTO tmpData = new ToPayOrderDTO();
                        tmpData.setReceipt(toPayItem.getReceiverName());
                        tmpData.setImgUrl(toPayItem.getGoodsList().get(0).getGoodsPic());
                        tmpData.setOrderNo(toPayItem.getOrderId());
                        StringBuilder attr = new StringBuilder();
                        attr.append("收件人:").append(toPayItem.getReceiverName()).append(";名称:").append(toPayItem.getGoodsList().get(0).getGoodsName()).append(toPayItem.getGoodsList().get(0).getSpecTitle()).append(";价格:").append(toPayItem.getPrice()).append(";下单时间:").append(dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(toPayItem.getCreateTime()), ZoneId.systemDefault())));
                        tmpData.setDesc(attr.toString());
                        toPayOrderDTOS.add(tmpData);
                    });
                }
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return toPayOrderDTOS;
    }

    /**
     * 添加意向单信息
     *
     * @param intendOrderInfo
     * @param auth
     * @return
     */
    public static List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> getToBuyGoodAndAddressInfoList(IntendOrderDTO intendOrderInfo, String auth) {
        initMapInfoByAuth(auth);
        if(intendOrderInfo != null && intendOrderInfo.getReceptNameList() != null && !intendOrderInfo.getReceptNameList().isEmpty()){
            List<String> receiptNames = Arrays.asList(intendOrderInfo.getReceptNameList().split("\\,"));
            receiptNames.forEach(item -> {
                ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO tmpToBuy = new ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO();
                mapIntendToBuyGoodInfos.get(auth).add(tmpToBuy);
                //intendToBuyGoods.add(tmpToBuy);
                tmpToBuy.setOrderDate(dateTimeFormatter.format(LocalDateTime.now()));
                tmpToBuy.setLocalNo(UUID.randomUUID().toString());
                tmpToBuy.setKdtSession(auth);
                tmpToBuy.setShotGoodName(intendOrderInfo.getGoodShotName());

                tmpToBuy.setToBuyNum(intendOrderInfo.getGoodNum());
                tmpToBuy.setNeedPresale(intendOrderInfo.isNeedPresale());
                tmpToBuy.setToBuySellType(intendOrderInfo.getToBuySellType());
                StringBuilder descSB = new StringBuilder("名称:");
                descSB.append(intendOrderInfo.getGoodShotName());
                if(mapAddressInfos.get(auth) != null){
                    ShoppingForAppDTO.AddressDataRowDetailDTO addressDetailInfo = mapAddressInfos.get(auth).stream().filter(address -> address.getAddressId().equalsIgnoreCase(item)).findFirst().orElse(null);
                    if(addressDetailInfo != null){
                        tmpToBuy.setAddressDetailInfo(addressDetailInfo);
                        tmpToBuy.setReceiptName(addressDetailInfo.getReceiveName());
                        descSB.append(" 收件人:").append(addressDetailInfo.getReceiveName());
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
                //descSB.append(" 预售:").append(intendOrderInfo.isNeedPresale() ? "是":"否").append(" 时间:").append(tmpToBuy.getOrderDate());
                String preSellDesc = "不限";
                if(intendOrderInfo.getToBuySellType() == 1){
                    preSellDesc = "现货";
                }else if(intendOrderInfo.getToBuySellType() == 2){
                    preSellDesc = "预售";
                }
                descSB.append(" 预售:").append(preSellDesc).append(" 时间:").append(tmpToBuy.getOrderDate());
                tmpToBuy.setDesc(descSB.toString());
            });
        }
        return mapIntendToBuyGoodInfos.get(auth);
    }

    /**
     * 主流程--自动下单
     * 循环逻辑: 从意向单中获取订单 --> 获取商品信息 --> 匹配订单信息 --> 下单成功后清除意向单信息
     *
     * @param auth
     * @param memberId
     * @param localNos
     * @return
     */
    public static String startAutoShopping(String auth, String memberId, String localNos) {
        initMapInfoByAuth(auth);
        StringBuilder resultBuilder = new StringBuilder();
        String standToBuyLocalNos = mapToBuyGoodAndAddressInfos.get(auth).stream().map(ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO::getLocalNo).collect(Collectors.joining(","));
        if(mapIntendToBuyGoodInfos.get(auth).size() > 0){
            List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> needToAddIntends = mapIntendToBuyGoodInfos.get(auth).stream().filter(item -> localNos.contains(item.getLocalNo()) && !standToBuyLocalNos.contains(item.getLocalNo())).collect(Collectors.toList());
            if(needToAddIntends != null && !needToAddIntends.isEmpty()){
                mapToBuyGoodAndAddressInfos.get(auth).addAll(needToAddIntends);
            }else {
                return "mission added";
            }
        }else {
            return "no intention order";
        }
        //buildToBuyAddressInfo(mapToBuyGoodAndAddressInfos.get(kdtSession),kdtSession,kdtId);
        if(mapStartShoppingSymbol.get(auth)){
            return "ordering";
        }
        //自旋下单流程
        for(long i=0;;i++){
            mapStartShoppingSymbol.put(auth,true);
            //System.out.println(resultBuilder.toString());
            try {
                ShoppingForAppBll.buildToBuyGoodInfo(mapToBuyGoodAndAddressInfos.get(auth),auth);
                //System.out.println(Calendar.getInstance().toInstant().toString() +" "+ i +" times buildToBuyGoodInfo mapToBuyGoodAndAddressInfos:"+ JSONObject.toJSONString(mapToBuyGoodAndAddressInfos.get(kdtSession)));
                //System.out.println(Calendar.getInstance().toInstant().toString() +" "+ i +" times buildToBuyGoodInfo mapToBuyGoodAndAddressInfos:"+ JSONObject.toJSONString(mapToBuyGoodAndAddressInfos.get(kdtSession)));
                ShoppingForAppBll.commitToBuyOrder(mapToBuyGoodAndAddressInfos.get(auth),auth);
                ShoppingForAppBll.removeAlreadyBuyAndToPayGood(mapToBuyGoodAndAddressInfos.get(auth),mapAlreadyBuyGoodAndAddressInfos.get(auth),mapIntendToBuyGoodInfos.get(auth));
            }catch (Exception ex){
                ex.printStackTrace();
                resultBuilder.append(ex.getMessage());
            }
            if(mapToBuyGoodAndAddressInfos.get(auth).isEmpty()){
                resultBuilder.append("ordered success:").append(MessageFormat.format("order number: {0} , details：{1}",mapAlreadyBuyGoodAndAddressInfos.get(auth).size(),JSONObject.toJSONString(mapAlreadyBuyGoodAndAddressInfos.get(auth))));
                System.out.println(resultBuilder.toString());
                mapStartShoppingSymbol.put(auth,false);
                return resultBuilder.toString();
            }
        }
    }

    public static boolean syncAddress(String auth, String memberId) {
        ShopInfoDTO.AddressInfoDTO addressInfo = getAllAddressInfo("YZ588805925511446528YZoNbQsG4R","42536286");
        if(addressInfo != null && addressInfo.addressList != null){
            List<ShoppingForAppDTO.AddressDataRowDetailDTO> addressDetailInfoDTOS = mapAddressInfos.get(auth);
            if(addressDetailInfoDTOS == null){
                addressDetailInfoDTOS = new ArrayList<>();
            }
            List<ShoppingForAppDTO.AddressDataRowDetailDTO> toAddAddress = new ArrayList<>();
            List<ShoppingForAppDTO.AddressDataRowDetailDTO> finalAddressDetailInfoDTOS1 = addressDetailInfoDTOS;
            addressInfo.addressList.forEach(item -> {
                if(finalAddressDetailInfoDTOS1.stream().noneMatch(address -> address.getReceivePhone().equalsIgnoreCase(item.tel))){
                    ShoppingForAppDTO.AddressDataRowDetailDTO tmpInfo = new ShoppingForAppDTO.AddressDataRowDetailDTO();
                    tmpInfo.setReceiveName(item.userName);
                    tmpInfo.setReceivePhone(item.tel);
                    tmpInfo.setProvince(item.province);
                    tmpInfo.setCity(item.city);
                    tmpInfo.setAddress(item.addressDetail);
                    tmpInfo.setArea(item.county);
                    toAddAddress.add(tmpInfo);
                }
            });
            if(toAddAddress != null && !toAddAddress.isEmpty()){
                toAddAddress.forEach(address -> {
                    try {
                        //{0}/address?receiveName={1}&receivePhone={2}&province={3}&city={4}&area={5}&address={6}&def=true
                        RequestBllForApp.doPost(MessageFormat.format(addAddressForApp, memberId,URLEncoder.encode(address.getReceiveName(),"utf-8"),
                                URLEncoder.encode(address.getReceivePhone(),"utf-8"),URLEncoder.encode(address.getProvince(),"utf-8"),URLEncoder.encode(address.getCity(),"utf-8"),URLEncoder.encode(address.getArea(),"utf-8"),URLEncoder.encode(address.getAddress(),"utf-8")),null,auth);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        return true;
    }
}
