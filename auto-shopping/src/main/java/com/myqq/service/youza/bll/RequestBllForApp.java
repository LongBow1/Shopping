package com.myqq.service.youza.bll;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.entity.ShoppingForAppDTO;
import com.myqq.service.youza.entity.ShoppingForWeChatAppDTO;
import com.myqq.service.youza.entity.ToBuyGoodInfoAppDTO;
import com.myqq.service.youza.util.TimeUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.myqq.service.youza.bll.ConstInfo.*;
import static com.myqq.service.youza.constinfo.ConstInfoForApp.commitOrderUrlForApp;
import static com.myqq.service.youza.constinfo.ConstInfoForApp.userAgentForApp;

public class RequestBllForApp {

    static Pattern pattern = Pattern.compile("\\s*|\\t|\\r|\\n");
    static RequestConfig appRequestConfig = RequestConfig.custom().setConnectTimeout(8000).setConnectionRequestTimeout(3000).setSocketTimeout(8000).build();

    public static String replaceBlank(String str){
        String res = "";
        if(str != null){
            Matcher matcher = pattern.matcher(str);
            res = matcher.replaceAll("");
        }
        return res;
    }

    public static void main(String[] args){
        String address = "\n河南许昌禹州市颍川街道禹州市顺杨厨卫批发\n马依琳13619889195";
        System.out.println(address);
        System.out.println("after");
        System.out.println(replaceBlank(address));
        /*String postDataFormat = "{\n" +
                "    \"province\": \"%s\",\n" +
                "    \"city\": \"%s\",\n" +
                "    \"area\": \"%s\",\n" +
                "    \"street\": \"%s\",\n" +
                "    \"houseNumber\": \"%s\",\n" +
                "    \"zipCode\": \"\",\n" +
                "    \"receiverName\": \"%s\",\n" +
                "    \"receiverPhone\": \"%s\",\n" +
                "    \"postFee\": 0,\n" +
                "    \"remark\": \"\",\n" +
                "    \"goodsList\": [\n" +
                "        {\n" +
                "            \"goodId\": \"%s\",\n" +
                "            \"count\": %d\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        StringBuilder commitOrderContentSB = new StringBuilder(String.format(postDataFormat, "0", "1", "2", "3", "4", "5", "8", "6", 7));
        System.out.println(commitOrderContentSB.toString());*/

        try {
            System.out.println(URLEncoder.encode("测试的","utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static String doGet(String url, String auth) {
        HttpGet httpGet = new HttpGet(url);
        //CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        CloseableHttpClient closeableHttpClient = HttpClients.custom().setDefaultRequestConfig(appRequestConfig).build();
        httpGet.addHeader("contentType", contentTypeJson);
        httpGet.addHeader("User-Agent",userAgentForApp);
        httpGet.addHeader("Authorization", auth);
        //httpGet.addHeader("Cookie", MessageFormat.format(ConstInfoForApp.cookieFormat, ConstInfoForApp.qqAuth));
        String res = null;
        try {
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            res = EntityUtils.toString(httpResponse.getEntity());
            //System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String doPatch(String url, String auth) {
        HttpPatch httpPatch = new HttpPatch(url);
        //CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        CloseableHttpClient closeableHttpClient = HttpClients.custom().setDefaultRequestConfig(appRequestConfig).build();
        httpPatch.addHeader("contentType", contentTypeJson);
        httpPatch.addHeader("User-Agent",userAgentForApp);
        httpPatch.addHeader("Authorization", auth);
        //httpPatch.addHeader("Cookie", MessageFormat.format(ConstInfoForApp.cookieFormat, ConstInfoForApp.qqAuth));
        String res = null;

        try {
            HttpResponse httpResponse = closeableHttpClient.execute(httpPatch);
            res = EntityUtils.toString(httpResponse.getEntity());
            //System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String doPost(String postUrl, StringEntity stringEntity, String auth){
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.addHeader("Content-type", contentTypeJson);
        httpPost.addHeader("User-Agent",userAgentForApp);
        httpPost.addHeader("Authorization", auth);

        String res = null;

        /*if(Calendar.getInstance().compareTo(calendarClock) <=0 ){
            return "{\"code\":101305009,\"msg\":\"商品:8222#背带裤套装，现货( 黑色 L ) : 商品未开售; 请重新选择\"}";
        }*/
        try {
            if(stringEntity == null){
                stringEntity = new StringEntity("");
            }
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            res = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(TimeUtil.getCurrentTimeString() +" commitOrder entity:"+ EntityUtils.toString(stringEntity) +" result:"+res);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String doPostForShoppingCar(String postUrl, StringEntity stringEntity, String auth){
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.addHeader("Content-type", contentTypeJson);
        httpPost.addHeader("User-Agent",userAgentForApp);
        httpPost.addHeader("Authorization", auth);

        String res = null;

        try {
            if(stringEntity == null){
                stringEntity = new StringEntity("");
            }
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            res = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(TimeUtil.getCurrentTimeString() +" shoppingCar commitOrder entity:"+ EntityUtils.toString(stringEntity) +" result:"+res);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static StringEntity getCommitPostEntity(ShoppingForAppDTO.GoodDataStockDetailDTO buyGood, ShoppingForAppDTO.AddressDataRowDetailDTO addressDetailInfo, int toBuyNum) {
        String operationInfo = "";
        StringEntity entity = null;
        try {
            String postDataFormat = "{\n" +
                    "    \"province\": \"%s\",\n" +
                    "    \"city\": \"%s\",\n" +
                    "    \"area\": \"%s\",\n" +
                    //"    \"street\": \"%s\",\n" +
                    "    \"houseNumber\": \"%s\",\n" +
                    "    \"zipCode\": \"\",\n" +
                    "    \"receiverName\": \"%s\",\n" +
                    "    \"receiverPhone\": \"%s\",\n" +
                    "    \"postFee\": 0,\n" +
                    "    \"remark\": \"\",\n" +
                    "    \"goodsList\": [\n" +
                    "        {\n" +
                    "            \"goodId\": \"%s\",\n" +
                    "            \"count\": %d\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            StringBuilder commitOrderContentSB = new StringBuilder(String.format(postDataFormat, addressDetailInfo.getProvince(), addressDetailInfo.getCity(), addressDetailInfo.getArea(),  replaceBlank(addressDetailInfo.getAddress()), addressDetailInfo.getReceiveName(), addressDetailInfo.getReceivePhone(), buyGood.getGoodsId(), toBuyNum));
            entity = new StringEntity(commitOrderContentSB.toString(),"utf-8");
        }catch (Exception ex){
            operationInfo += ex.getMessage();
        }
        if(!operationInfo.isEmpty()){
            System.out.println(TimeUtil.getCurrentTimeString() + "getCommitPostEntity operationInfo: "+operationInfo);
        }
        return entity;
    }

    /**
     * 提交下单
     *
     * @param toBuy
     * @param buyGood
     * @param commitPostEntity
     * @param auth
     * @param testMode 1= 模拟80005,2- 模拟80001
     * @return
     */
    public static String commitOrderDetail(ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO toBuy, ShoppingForAppDTO.GoodDataStockDetailDTO buyGood, StringEntity commitPostEntity, String auth, int testMode, String memberId) {
        String operationInfo = "";
        String orderInfo;
        if(testMode == 1){
            orderInfo = "{\"code\":80005,\"message\":\"商品还未开始抢购\",\"status\":400,\"data\":\"ORDER_GOODS_ONSTART\"}";
            System.out.println(orderInfo);
        }else if(testMode == 2){
            orderInfo = "{\"code\":80001,\"message\":\"库存不足\",\"status\":400,\"data\":\"ORDER_STOCK_INSUFFICIENT\"}";
            System.out.println(orderInfo);
        }else {
            orderInfo = doPost(commitOrderUrlForApp, commitPostEntity, auth);
        }

        if(orderInfo != null){
            try {
                ShoppingForAppDTO.CommitOrderDTO commitOrderInfo = JSONObject.parseObject(orderInfo, ShoppingForAppDTO.CommitOrderDTO.class);
                if(commitOrderInfo != null && commitOrderInfo.getData() != null && commitOrderInfo.getData().getOrderId() != null){
                    if(commitOrderInfo.getData() != null){
                        buyGood.setOrderNo(commitOrderInfo.getData().getOrderId());
                        AutoShoppingEntryForApp.mapOrderedCounter.put(memberId,AutoShoppingEntryForApp.mapOrderedCounter.get(memberId)-1);
                    }
                    if(toBuy.getCommitOrderInfoList() == null){
                        toBuy.setCommitOrderInfoList(new ArrayList<>());
                    }
                    toBuy.getCommitOrderInfoList().add(commitOrderInfo);
                }
            }catch (Exception ex){
                //ex.printStackTrace();
                try {
                    ShoppingForAppDTO.CommitOrderErrorDTO commitOrderError = JSONObject.parseObject(orderInfo, ShoppingForAppDTO.CommitOrderErrorDTO.class);
                    System.out.println(TimeUtil.getCurrentTimeString() + " commitOrderError: " + commitOrderError.toString());
                    if(commitOrderError != null){
                        //80005 未开始抢，这个时候不用重新查询库存信息。 多抢几次再查询
                        int count = 0;
                        while (Optional.ofNullable(commitOrderError.getCode()).orElse(0) == 80005 && count<6){
                            commitOrderError = JSONObject.parseObject(commitOrderDetailV2(toBuy, buyGood, commitPostEntity, auth,testMode, memberId), ShoppingForAppDTO.CommitOrderErrorDTO.class);
                            count++;
                            System.out.println(count);
                        }
                        //80001提交下单后提示库存不足，重复提交多次，不用重新查询库存
                        while (Optional.ofNullable(commitOrderError.getCode()).orElse(0) == 80001 && count<10){
                            commitOrderError = JSONObject.parseObject(commitOrderDetailV2(toBuy, buyGood, commitPostEntity, auth,testMode, memberId), ShoppingForAppDTO.CommitOrderErrorDTO.class);
                            count++;
                            System.out.println(count);
                        }
                    }
                }catch (Exception ex1){
                    ex1.printStackTrace();
                }
                operationInfo = ex.getMessage();
            }
        }
        if(!operationInfo.isEmpty()){
            System.out.println(TimeUtil.getCurrentTimeString() +" commitToBuyOrder operationInfo: "+ operationInfo);
        }
        return orderInfo;
    }

    public static String commitOrderDetail(String commitUrl, ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO toBuy, ShoppingForAppDTO.GoodDataStockDetailDTO buyGood, StringEntity commitPostEntity, String auth, int testMode, String memberId) {
        String operationInfo = "";
        String orderInfo;
        if(testMode == 1){
            orderInfo = "{\"code\":80005,\"message\":\"商品还未开始抢购\",\"status\":400,\"data\":\"ORDER_GOODS_ONSTART\"}";
            System.out.println(orderInfo);
        }else if(testMode == 2){
            orderInfo = "{\"code\":80001,\"message\":\"库存不足\",\"status\":400,\"data\":\"ORDER_STOCK_INSUFFICIENT\"}";
            System.out.println(orderInfo);
        }else {
            orderInfo = doPost(commitUrl, commitPostEntity, auth);
        }

        if(orderInfo != null){
            try {
                ShoppingForAppDTO.CommitOrderDTO commitOrderInfo = JSONObject.parseObject(orderInfo, ShoppingForAppDTO.CommitOrderDTO.class);
                if(commitOrderInfo != null && commitOrderInfo.getData() != null && commitOrderInfo.getData().getOrderId() != null){
                    if(commitOrderInfo.getData() != null){
                        buyGood.setOrderNo(commitOrderInfo.getData().getOrderId());
                        AutoShoppingEntryForWeChatApp.mapOrderedCounter.put(memberId,AutoShoppingEntryForWeChatApp.mapOrderedCounter.get(memberId)-1);
                    }
                    if(toBuy.getCommitOrderInfoList() == null){
                        toBuy.setCommitOrderInfoList(new ArrayList<>());
                    }
                    toBuy.getCommitOrderInfoList().add(commitOrderInfo);
                }
            }catch (Exception ex){
                //ex.printStackTrace();
                try {
                    ShoppingForAppDTO.CommitOrderErrorDTO commitOrderError = JSONObject.parseObject(orderInfo, ShoppingForAppDTO.CommitOrderErrorDTO.class);
                    System.out.println(TimeUtil.getCurrentTimeString() + " commitOrderError: " + commitOrderError.toString());
                    if(commitOrderError != null){
                        //80005 未开始抢，这个时候不用重新查询库存信息。 多抢几次再查询
                        int count = 0;
                        while (Optional.ofNullable(commitOrderError.getCode()).orElse(0) == 80005 && count<6){
                            commitOrderError = JSONObject.parseObject(commitOrderDetailV2(toBuy, buyGood, commitPostEntity, auth,testMode, memberId), ShoppingForAppDTO.CommitOrderErrorDTO.class);
                            count++;
                            System.out.println(count);
                        }
                        //80001提交下单后提示库存不足，重复提交多次，不用重新查询库存
                        while (Optional.ofNullable(commitOrderError.getCode()).orElse(0) == 80001 && count<10){
                            commitOrderError = JSONObject.parseObject(commitOrderDetailV2(toBuy, buyGood, commitPostEntity, auth,testMode, memberId), ShoppingForAppDTO.CommitOrderErrorDTO.class);
                            count++;
                            System.out.println(count);
                        }
                    }
                }catch (Exception ex1){
                    ex1.printStackTrace();
                }
                operationInfo = ex.getMessage();
            }
        }
        if(!operationInfo.isEmpty()){
            System.out.println(TimeUtil.getCurrentTimeString() +" commitToBuyOrder operationInfo: "+ operationInfo);
        }
        return orderInfo;
    }

    public static String commitOrderDetailV2(ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO toBuy, ShoppingForAppDTO.GoodDataStockDetailDTO buyGood, StringEntity commitPostEntity, String auth, int testMode, String memberId) {
        String operationInfo = "";
        String orderInfo;
        if(testMode == 1){
            orderInfo = "{\"code\":80005,\"message\":\"商品还未开始抢购\",\"status\":400,\"data\":\"ORDER_GOODS_ONSTART\"}";
        }else if(testMode == 2){
            orderInfo = "{\"code\":80001,\"message\":\"库存不足\",\"status\":400,\"data\":\"ORDER_STOCK_INSUFFICIENT\"}";
        }else {
            orderInfo = doPost(commitOrderUrlForApp, commitPostEntity, auth);
        }

        if(orderInfo != null){
            try {
                ShoppingForAppDTO.CommitOrderDTO commitOrderInfo = JSONObject.parseObject(orderInfo, ShoppingForAppDTO.CommitOrderDTO.class);
                if(commitOrderInfo != null && commitOrderInfo.getData() != null){
                    if(commitOrderInfo.getData() != null){
                        buyGood.setOrderNo(commitOrderInfo.getData().getOrderId());
                        AutoShoppingEntryForApp.mapOrderedCounter.put(memberId,AutoShoppingEntryForApp.mapOrderedCounter.get(memberId)-1);
                    }
                    if(toBuy.getCommitOrderInfoList() == null){
                        toBuy.setCommitOrderInfoList(new ArrayList<>());
                    }
                    toBuy.getCommitOrderInfoList().add(commitOrderInfo);
                }
            }catch (Exception ex){
                //System.out.println(ex.getMessage());
                operationInfo = ex.getMessage();
            }
        }
        if(!operationInfo.isEmpty()){
            System.out.println(TimeUtil.getCurrentTimeString() +" commitToBuyOrder operationInfo: "+ operationInfo);
        }
        return orderInfo;
    }



    public static StringEntity getPromotionPostEntity(List<ShoppingForAppDTO.GoodDataStockDetailDTO> buyGoods, ShoppingForAppDTO.AddressDataRowDetailDTO addressDetailInfo) {
        String operationInfo = "";
        StringEntity entity = null;
        try {
            String postDataFormat = "{\n" +
                    "    \"province\": \"%s\",\n" +
                    "    \"city\": \"%s\",\n" +
                    "    \"area\": \"%s\",\n" +
                    //"    \"street\": \"%s\",\n" +
                    "    \"houseNumber\": \"%s\",\n" +
                    "    \"zipCode\": \"\",\n" +
                    "    \"receiverName\": \"%s\",\n" +
                    "    \"receiverPhone\": \"%s\",\n" +
                    "    \"postFee\": 0,\n" +
                    "    \"remark\": \"\",\n" +
                    "    \"goodsList\": [\n %s" +
                    "    ]\n" +
                    "}";
            String postFormat = "{\"city\":\"%s\"," +
                    "\"goodsList\":[%s]," +
                    "\"houseNumber\":\"%s\"," +
                    "\"province\":\"%s\"," +
                    "\"receiverName\":\"%s\"," +
                    "\"receiverPhone\":\"%s\"}";
            StringBuilder goodDetailListBuilder = new StringBuilder();
            buyGoods.forEach(good -> {
                goodDetailListBuilder.append("{\"goodId\":").append("\""+good.getGoodsId()+"\"").append(",").append("\"count\":").append(good.getToBuyNum()).append("},");
            });
            String goodDetailStr = goodDetailListBuilder.toString().substring(0,goodDetailListBuilder.toString().length()-1);
            StringBuilder commitOrderContentSB = new StringBuilder(String.format(postFormat, addressDetailInfo.getCity(), goodDetailStr,  replaceBlank(addressDetailInfo.getAddress()),addressDetailInfo.getProvince(), addressDetailInfo.getReceiveName(), addressDetailInfo.getReceivePhone()));
            entity = new StringEntity(commitOrderContentSB.toString(),"utf-8");
        }catch (Exception ex){
            operationInfo += ex.getMessage();
        }
        if(!operationInfo.isEmpty()){
            System.out.println(TimeUtil.getCurrentTimeString() + "getCommitPostEntity for promotion operationInfo: "+operationInfo);
        }
        return entity;
    }

    /**
     * 马哥精选post取消订单
     *
     * @param orderID
     * @param reason
     * @return
     */
    public static StringEntity getCancelOrderForMagePostEntity(String orderID, String reason) {
        String operationInfo = "";
        StringEntity entity = null;
        try {
            String postFormat = "{\"orderId\":\"%s\"," +
                    "\"reason\":\"%s\"}";
            StringBuilder commitOrderContentSB = new StringBuilder(String.format(postFormat, orderID, reason));
            entity = new StringEntity(commitOrderContentSB.toString(),"utf-8");
        }catch (Exception ex){
            operationInfo += ex.getMessage();
        }
        if(!operationInfo.isEmpty()){
            System.out.println(TimeUtil.getCurrentTimeString() + "getCommitPostEntity for promotion operationInfo: "+operationInfo);
        }
        return entity;
    }

}
