package com.myqq.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.bll.AutoShoppingEntry;
import com.myqq.service.youza.bll.AutoShoppingEntryForApp;
import com.myqq.service.youza.bll.WeChatBll;
import com.myqq.service.youza.entity.IntendOrderDTO;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.myqq.service.youza.constinfo.ConstInfoForApp.*;

@RestController
@EnableAutoConfiguration
@CrossOrigin
@RequestMapping("orderApp")
public class OrderForAppController {
    @RequestMapping("/sayHello")
    public String sayHello(){
        return "hello success!";
    }


    @RequestMapping("testConnect")
    public boolean testConnect(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        boolean testRes = AutoShoppingEntryForApp.testConnect(auth, memberId);
        return testRes;
    }

    @RequestMapping("getAuth")
    public String getAuthInfo(@RequestParam("username")String userName, @RequestParam("password")String password){
        return AutoShoppingEntryForApp.getAuthInfo(userName, password);
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
        return AutoShoppingEntryForApp.getAddressInfo(auth, memberId, useLocalAddress);
    }

    @RequestMapping("/getIntendOrderList")
    public String getIntendOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForApp.mapIntendToBuyGoodInfos.get(memberId));
    }

    @RequestMapping("/deleteIntendOrderList")
    public String deleteIntendOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId, @RequestParam("localNos")String localNos){
        return JSONObject.toJSONString(AutoShoppingEntryForApp.deleteIntendOrders(memberId, localNos));
    }

    @RequestMapping("/getAlreadyBuyOrderList")
    public String getAlreadyBuyOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForApp.mapAlreadyBuyGoodAndAddressInfos.get(memberId));
    }

    @RequestMapping("/getReadyToBuyOrderList")
    public String getReadyToBuyOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForApp.mapToBuyGoodAndAddressInfos.get(memberId));
    }

    @RequestMapping("/getToPayOrderList")
    public String getToPayOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForApp.getToPayOrderList(auth, memberId, getToPayGoodDetailUrlForApp));
    }

    @RequestMapping("/getToSendOrderList")
    public String getToSendOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForApp.getToPayOrderList(auth, memberId, getToSendGoodUrlForApp));
    }
    @RequestMapping("/getSendedOrderList")
    public String getSendedOrderList(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return JSONObject.toJSONString(AutoShoppingEntryForApp.getToPayOrderList(auth, memberId, getSendGoodUrlForApp));
    }

    @RequestMapping("/clearAllOrderInfo")
    public boolean clearAllOrderInfo(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return AutoShoppingEntryForApp.clearAllOrderInfo(memberId);
    }

    @RequestMapping("/stopCommitOrder")
    public boolean stopCommitOrder(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId){
        return AutoShoppingEntryForApp.clearStandToBuyOrderInfo(memberId);
    }

    @RequestMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("auth")String auth, @RequestParam("memberId")String memberId, @RequestParam("orderNos") String orderNos){
        String opResult = AutoShoppingEntryForApp.cancelOrder(orderNos, auth, memberId);
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
        String resultJson = JSONObject.toJSONString(AutoShoppingEntryForApp.getToBuyGoodAndAddressInfoList(intendOrderInfo, memberId));
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
        String result = AutoShoppingEntryForApp.startAutoShopping(auth, memberId, localNos);
        return result;
    }

    /**
     * 同步地址
     *
     * @return
     */
    @RequestMapping("/syncAddress")
    public boolean syncAddress(){
        return AutoShoppingEntryForApp.syncAddress(zzAuth, zzMemberId);
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
}
