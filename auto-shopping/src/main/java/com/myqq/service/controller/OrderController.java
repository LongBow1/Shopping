package com.myqq.service.controller;


import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.bll.AutoShoppingEntry;
import com.myqq.service.youza.entity.IntendOrderDTO;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

@RestController
@EnableAutoConfiguration
@CrossOrigin
@RequestMapping("/order")
public class OrderController {

    static String kdtSession = "YZ575840077289181184YZL1i1uMFh";

    /*static {
        AutoShoppingEntry.startOrderJob(kdtSession);
    }*/


    @RequestMapping("/sayHello")
    public String sayHello(){
        return "hello success!";
    }

    @RequestMapping("/testConnect")
    public boolean testConnectCorrect(@RequestParam("session")String kdtSession, @RequestParam("shopInfo")String shopInfo, HttpServletRequest request){
        this.kdtSession = kdtSession;
        //HttpServletRequest request = RequestContextHolder.getRequestAttributes().getRequest();
        return AutoShoppingEntry.testConnect(kdtSession,AutoShoppingEntry.getIdByIndex(shopInfo,0));
    }

    @RequestMapping("/getAddress")
    public String getAllAddress(@RequestParam("session")String kdtSession, @RequestParam("shopInfo")String shopInfo){
        return AutoShoppingEntry.getAddressInfo(kdtSession,AutoShoppingEntry.getIdByIndex(shopInfo,0));
    }

    @RequestMapping("/getIntendOrderList")
    public String getIntendOrderList(@RequestParam("session")String kdtSession, @RequestParam("shopInfo")String shopInfo){
        return JSONObject.toJSONString(AutoShoppingEntry.mapIntendToBuyGoodInfos.get(kdtSession));
    }

    @RequestMapping("/deleteIntendOrderList")
    public String deleteIntendOrderList(@RequestParam("session")String kdtSession,@RequestParam("localNos")String localNos, @RequestParam("shopInfo")String shopInfo){
        return JSONObject.toJSONString(AutoShoppingEntry.deleteIntendOrders(kdtSession,localNos));
    }

    @RequestMapping("/getAlreadyBuyOrderList")
    public String getAlreadyBuyOrderList(@RequestParam("session")String kdtSession, @RequestParam("shopInfo")String shopInfo){
        return JSONObject.toJSONString(AutoShoppingEntry.mapAlreadyBuyGoodAndAddressInfos.get(kdtSession));
    }

    @RequestMapping("/getReadyToBuyOrderList")
    public String getReadyToBuyOrderList(@RequestParam("session")String kdtSession, @RequestParam("shopInfo")String shopInfo){
        return JSONObject.toJSONString(AutoShoppingEntry.mapToBuyGoodAndAddressInfos.get(kdtSession));
    }

    @RequestMapping("/getToPayOrderList")
    public String getToPayOrderList(@RequestParam("session")String kdtSession, @RequestParam("shopInfo")String shopInfo){
        return JSONObject.toJSONString(AutoShoppingEntry.getToPayOrderList(kdtSession,AutoShoppingEntry.getIdByIndex(shopInfo,0)));
    }

    @RequestMapping("/getToSendOrderList")
    public String getToSendOrderList(@RequestParam("session")String kdtSession, @RequestParam("shopInfo")String shopInfo){
        return JSONObject.toJSONString(AutoShoppingEntry.getToSendOrderList(kdtSession,AutoShoppingEntry.getIdByIndex(shopInfo,0)));
    }
    @RequestMapping("/getSendedOrderList")
    public String getSendedOrderList(@RequestParam("session")String kdtSession, @RequestParam("shopInfo")String shopInfo){
        return JSONObject.toJSONString(AutoShoppingEntry.getSendedOrderList(kdtSession,AutoShoppingEntry.getIdByIndex(shopInfo,0)));
    }
    @RequestMapping("/createIntendOrder")
    public String createIntendOrder(@RequestParam("session")String kdtSession, @RequestParam("intendOrderInfo") String intendOrderInfoStr, @RequestParam("shopInfo")String shopInfo){
        IntendOrderDTO intendOrderInfo = null;
        try{
            intendOrderInfo = JSONObject.parseObject(intendOrderInfoStr, IntendOrderDTO.class);
        }catch (Exception ex){
            return ex.getMessage();
        }
        if(intendOrderInfo == null){
            return "无法解析到意向单信息";
        }
        String resultJson = JSONObject.toJSONString(AutoShoppingEntry.getToBuyGoodAndAddressInfoList(intendOrderInfo,kdtSession));
        return resultJson;
    }

    @RequestMapping("/clearAllOrderInfo")
    public boolean clearAllOrderInfo(@RequestParam("session")String kdtSession, @RequestParam("shopInfo")String shopInfo){
        return AutoShoppingEntry.clearAllOrderInfo(kdtSession);
    }

    @RequestMapping("/stopCommitOrder")
    public boolean stopCommitOrder(@RequestParam("session")String kdtSession, @RequestParam("shopInfo")String shopInfo){
        return AutoShoppingEntry.clearStandToBuyOrderInfo(kdtSession);
    }

    @RequestMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("session")String kdtSession,@RequestParam("orderNos") String orderNos, @RequestParam("shopInfo")String shopInfo){
        AutoShoppingEntry.cancelOrder(null,kdtSession,AutoShoppingEntry.getIdByIndex(shopInfo,0));
        return "取消成功";
    }

    @RequestMapping("/autoShopping")
    public String startAutoShopping(@RequestParam("session")String kdtSession, @RequestParam("localNos")String localNos, @RequestParam("shopInfo")String shopInfo){
        String result = AutoShoppingEntry.startAutoShopping(kdtSession, localNos,AutoShoppingEntry.getIdByIndex(shopInfo,0),AutoShoppingEntry.getIdByIndex(shopInfo,1),AutoShoppingEntry.getIdByIndex(shopInfo,2));
        return result;
    }
}
