package com.myqq.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.bll.AutoShoppingEntryForApp;
import com.myqq.service.youza.bll.AutoShoppingEntryForWeChatApp;
import com.myqq.service.youza.bll.WeChatBll;
import com.myqq.service.youza.entity.IntendOrderDTO;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.myqq.service.youza.bll.ShoppingForAppBll.quantifiers;
import static com.myqq.service.youza.constinfo.ConstInfoForWeChatApp.cancelOrderUrlForApp;
import static com.myqq.service.youza.constinfo.ConstInfoForWeChatApp.*;

/**
 * 微信小程序
 * 马哥精选
 */
@RestController
@EnableAutoConfiguration
@CrossOrigin
@RequestMapping("orderWeChatApp")
public class OrderForWeChatAppController {
    @RequestMapping("/sayHello")
    public String sayHello(){
        return "hello success!";
    }


    @RequestMapping("testConnect")
    public boolean testConnect(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        boolean testRes = AutoShoppingEntryForWeChatApp.testConnect(auth, memberId);
        return testRes;
    }

    @RequestMapping("getAuth")
    public String getAuthInfo(@RequestParam("username")String userName, @RequestParam("password")String password){
        return AutoShoppingEntryForWeChatApp.getAuthInfo(userName, password);
    }

    /**
     * 设置收件人信息
     *
     * @param auth
     * @param memberId
     * @return
     */
    @RequestMapping("/getAddress")
    public String getAllAddress(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId, @RequestParam("useLocalAddress")boolean useLocalAddress){
        return AutoShoppingEntryForWeChatApp.getAddressInfo(auth, memberId, useLocalAddress);
    }

    @RequestMapping("/deleteLocalAddress")
    public String deleteLocalAddress(@RequestParam("memberId")String memberId, @RequestParam("addressIds") String deleteAddressIds){
        return AutoShoppingEntryForWeChatApp.deleteLocalAddress(memberId, deleteAddressIds);
    }

    @RequestMapping("/getIntendOrderList")
    public String getIntendOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForWeChatApp.mapIntendToBuyGoodInfos.get(memberId));
    }

    @RequestMapping("/deleteIntendOrderList")
    public String deleteIntendOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId, @RequestParam("localNos")String localNos){
        return JSONObject.toJSONString(AutoShoppingEntryForWeChatApp.deleteIntendOrders(memberId, localNos));
    }

    @RequestMapping("/getAlreadyBuyOrderList")
    public String getAlreadyBuyOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForWeChatApp.mapAlreadyBuyGoodAndAddressInfos.get(memberId));
    }

    @RequestMapping("/getReadyToBuyOrderList")
    public String getReadyToBuyOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForWeChatApp.mapToBuyGoodAndAddressInfos.get(memberId));
    }

    @RequestMapping("/getToPayOrderList")
    public String getToPayOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForWeChatApp.getToPayOrderList(auth, memberId, getToPayGoodDetailUrlForApp));
    }

    @RequestMapping("/getToSendOrderList")
    public String getToSendOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForWeChatApp.getToPayOrderList(auth, memberId, getToSendGoodUrlForApp));
    }
    @RequestMapping("/getSendedOrderList")
    public String getSendedOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForWeChatApp.getToPayOrderList(auth, memberId, getSendGoodUrlForApp));
    }

    @RequestMapping("/clearAllOrderInfo")
    public boolean clearAllOrderInfo(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return AutoShoppingEntryForWeChatApp.clearAllOrderInfo(memberId);
    }

    @RequestMapping("/stopCommitOrder")
    public boolean stopCommitOrder(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return AutoShoppingEntryForWeChatApp.clearStandToBuyOrderInfo(memberId);
    }

    @RequestMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId, @RequestParam("orderNos") String orderNos){
        String opResult = AutoShoppingEntryForWeChatApp.cancelOrder(getToPayGoodDetailUrlForApp, cancelOrderUrlForApp, orderNos, auth, memberId);
        return opResult;
    }

    /**
     * 创建意向单
     * @param auth
     * @param memberId
     * @param intendOrderInfoStr
     * @return
     */
    @RequestMapping("/createIntendOrder")
    public String createIntendOrder(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId, @RequestParam("intendOrderInfo") String intendOrderInfoStr){
        IntendOrderDTO intendOrderInfo;
        try{
            intendOrderInfo = JSONObject.parseObject(intendOrderInfoStr, IntendOrderDTO.class);
        }catch (Exception ex){
            return ex.getMessage();
        }
        if(intendOrderInfo == null){
            return "unable to resolve intention order";
        }
        String resultJson = JSONObject.toJSONString(AutoShoppingEntryForWeChatApp.getToBuyGoodAndAddressInfoList(intendOrderInfo, memberId));
        return resultJson;
    }

    /**
     * 开始自动下单
     *
     * @param auth
     * @param memberId
     * @param localNos
     * @return
     */
    @RequestMapping("/autoShopping")
    public String startAutoShopping(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId, @RequestParam("localNos")String localNos){
        String result = AutoShoppingEntryForWeChatApp.startAutoShopping(auth, memberId, localNos);
        return result;
    }

    /**
     * 同步地址
     *
     * @return
     */
    @RequestMapping("/syncAddress")
    public boolean syncAddress(){
        return AutoShoppingEntryForWeChatApp.syncAddress(zzAuth, zzMemberId);
    }

    /**
     * 同步地址
     *
     * @return
     */
    @RequestMapping("/love")
    public String love(){
        return WeChatBll.loveZZ();
    }

    @RequestMapping("/getQuantifier")
    public String getQuantifier(){
        return JSONObject.toJSONString(quantifiers);
    }

    @RequestMapping("/addQuantifier")
    public String addQuantifier(@RequestParam("newQuantifier")String newQuantifier){
        if(!quantifiers.contains(newQuantifier)){
            quantifiers.add(newQuantifier);
        }
        return JSONObject.toJSONString(quantifiers);
    }
}
