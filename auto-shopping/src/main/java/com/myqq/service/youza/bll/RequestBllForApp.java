package com.myqq.service.youza.bll;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.constinfo.ConstInfoForApp;
import com.myqq.service.youza.entity.ShoppingForAppDTO;
import com.myqq.service.youza.entity.ToBuyGoodInfoAppDTO;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;

import static com.myqq.service.youza.bll.ConstInfo.*;
import static com.myqq.service.youza.constinfo.ConstInfoForApp.commitOrderUrlForApp;

public class RequestBllForApp {

    public static void main(String[] args){
        String postDataFormat = "{\n" +
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
        System.out.println(commitOrderContentSB.toString());

        try {
            System.out.println(URLEncoder.encode("测试的","utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static String doGet(String url, String auth) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        httpGet.addHeader("contentType", contentTypeJson);
        httpGet.addHeader("User-Agent",userAgent);
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
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        httpPatch.addHeader("contentType", contentTypeJson);
        httpPatch.addHeader("User-Agent",userAgent);
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
        httpPost.addHeader("User-Agent",userAgent);
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
            System.out.println("commitOrder entity:"+ EntityUtils.toString(stringEntity) +" result:"+res);
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
            StringBuilder commitOrderContentSB = new StringBuilder(String.format(postDataFormat, addressDetailInfo.getProvince(), addressDetailInfo.getCity(), addressDetailInfo.getArea(), addressDetailInfo.getAddress(), addressDetailInfo.getAddress(), addressDetailInfo.getReceiveName(), addressDetailInfo.getReceivePhone(), buyGood.getGoodsId(), toBuyNum));
            entity = new StringEntity(commitOrderContentSB.toString(),"utf-8");
        }catch (Exception ex){
            operationInfo += ex.getMessage();
        }
        if(!operationInfo.isEmpty()){
            System.out.println("getCommitPostEntity operationInfo: "+operationInfo);
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
     * @return
     */
    public static String commitOrderDetail(ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO toBuy, ShoppingForAppDTO.GoodDataStockDetailDTO buyGood, StringEntity commitPostEntity, String auth) {
        StringBuilder operationInfo = new StringBuilder("");
        String orderInfo = doPost(commitOrderUrlForApp, commitPostEntity, auth);
        if(orderInfo != null){
            try {
                ShoppingForAppDTO.CommitOrderDTO commitOrderInfo = JSONObject.parseObject(orderInfo, ShoppingForAppDTO.CommitOrderDTO.class);
                if(commitOrderInfo != null && commitOrderInfo.getData() != null){
                    if(commitOrderInfo.getData() != null){
                        buyGood.setOrderNo(commitOrderInfo.getData().getOrderId());
                    }
                    if(toBuy.getCommitOrderInfoList() == null){
                        toBuy.setCommitOrderInfoList(new ArrayList<>());
                    }
                    toBuy.getCommitOrderInfoList().add(commitOrderInfo);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                operationInfo.append(ex.getMessage());
            }
        }
        if(!operationInfo.toString().isEmpty()){
            System.out.println("commitToBuyOrder operationInfo: "+ operationInfo.toString());
        }
        return orderInfo;
    }
}
