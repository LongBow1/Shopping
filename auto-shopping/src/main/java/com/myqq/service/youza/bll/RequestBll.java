package com.myqq.service.youza.bll;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.myqq.service.youza.bll.ConstInfo.*;
import static com.myqq.service.youza.bll.ShopInfoDTO.*;

public class RequestBll {
    static Calendar calendarClock = Calendar.getInstance();
    static {
        calendarClock.add(Calendar.SECOND,30);
    }

    public static String doGet(String goodsUrl,String kdtSession, String kdtId) {
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(goodsUrl);

        httpGet.addHeader("contentType",contentType);
        httpGet.addHeader("User-Agent",userAgent);
        httpGet.addHeader("Cookie",MessageFormat.format(cookieFormat,kdtSession,kdtId));
        String res = null;

        try {
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            res = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println(res);
            //unescape("%u4E2D%u6587")  goods-data
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String doPost(String postUrl, StringEntity stringEntity, String kdtSession, String kdtId){
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.addHeader("Content-type",contentType);
        httpPost.addHeader("User-Agent",userAgent);
        httpPost.addHeader("Cookie", MessageFormat.format(cookieFormat,kdtSession,kdtId));

        String res = null;

        /*if(Calendar.getInstance().compareTo(calendarClock) <=0 ){
            return "{\"code\":101305009,\"msg\":\"商品:8222#背带裤套装，现货( 黑色 L ) : 商品未开售; 请重新选择\"}";
        }*/
        try {
            httpPost.setEntity((stringEntity));
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

    public static String doPost(String postUrl, List<NameValuePair> pairList,String kdtSession, String kdtId){
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(postUrl);

        httpPost.addHeader("contentType",contentType);
        httpPost.addHeader("User-Agent",userAgent);
        httpPost.addHeader("Cookie",MessageFormat.format(cookieFormat,kdtSession,kdtId));
        //pairList.add(new BasicNameValuePair("",""));
        String res = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairList));
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            res = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(res);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 表单风格取消订单
     * @param orderNo
     * @return
     */
    public static List<NameValuePair> getCancelOrderPostEntity(String orderNo,String kdtId){
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("order_no",orderNo));
        nameValuePairs.add(new BasicNameValuePair("kdt_id",kdtId));
        return nameValuePairs;
    }


    /**
     * 更新下单结果信息
     * @param toBuy
     * @param postUrl
     * @param toBuyGoodSkuInfo
     * @param stringEntity
     * @return
     */
    public static String commitOrderDetail(ToBuyGoodAndAddressInfo toBuy, String postUrl, ToBuyGoodSkuInfo toBuyGoodSkuInfo, StringEntity stringEntity, String kdtSession, String kdtId){
        String orderInfo = doPost(postUrl, stringEntity,kdtSession,kdtId);
        if(orderInfo != null){
            try {
                CommitOrderInfo commitOrderInfo = JSONObject.parseObject(orderInfo, CommitOrderInfo.class);
                if(commitOrderInfo != null && commitOrderInfo.getData() != null){
                    if(commitOrderInfo.getData() != null){
                        toBuyGoodSkuInfo.orderNo = commitOrderInfo.getData().getOrderNo();
                        commitOrderInfo.getData().setSkuInfo(toBuyGoodSkuInfo.sku);
                    }
                    if(toBuy.getCommitOrderInfoList() == null){
                        toBuy.setCommitOrderInfoList(new ArrayList<>());
                    }
                    toBuy.getCommitOrderInfoList().add(commitOrderInfo);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return orderInfo;
    }
}
