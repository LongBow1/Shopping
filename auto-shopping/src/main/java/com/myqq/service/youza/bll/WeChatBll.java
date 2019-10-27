package com.myqq.service.youza.bll;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;

import static com.myqq.service.youza.bll.ConstInfo.contentTypeUrl;

@Service
public class WeChatBll {

    static String appId = "wxd3d83c9777dc66d8";
    static String appSecret = "805daf72762211103e234acdc8a48623";

    public static void main(String[] args) {
        String accessTokenJson =getAccessToken(appId, appSecret);
        JSONObject jsonObject = JSONObject.parseObject(accessTokenJson);
        System.out.println(jsonObject);
        String accessToken = jsonObject.get("access_token").toString();
        System.out.println("accessToken:"+accessToken);
        sendMessage("test from local", accessToken);

    }

    public static String sendMessageTest(){
        String accessTokenJson =getAccessToken(appId, appSecret);
        JSONObject jsonObject = JSONObject.parseObject(accessTokenJson);
        System.out.println(jsonObject);
        String accessToken = jsonObject.get("access_token").toString();
        System.out.println("accessToken:"+accessToken);
        String res = sendMessage("test from local", accessToken);
        return "accessTokenJson:"+accessTokenJson + "\r\nres:"+res;
    }

    /**
     * get access_token from weChat server
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public static String getAccessToken(@NotEmpty String appId, @NotEmpty String appSecret) {
        String accessToken = "";
        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;

        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(tokenUrl);
        httpGet.addHeader("contentType", contentTypeUrl);
        try {
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            accessToken = EntityUtils.toString(httpResponse.getEntity());
            System.out.println("get weChat accessToken:" + accessToken);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return accessToken;
    }

    public static String sendMessage(String jsonString, String accessToken) {
        String postUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken;
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(postUrl);
        StringEntity stringEntity = new StringEntity(jsonString, "UTF-8");
        httpPost.setEntity(stringEntity);
        String res = "";
        try {
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            res = EntityUtils.toString(httpResponse.getEntity());
            System.out.println("sendWeChatMessage:" + res);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            res += ex.getMessage();
        }
        return res;

    }
}
