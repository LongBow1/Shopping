package com.myqq.service.controller;


import com.myqq.service.youza.bll.WeChatBll;
import com.myqq.service.youza.util.CheckUntil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableAutoConfiguration
public class WeChatController {

    @ResponseBody
    @RequestMapping("/home")
    String home(){
        return "weChat Success!";
    }

    @ResponseBody
    @RequestMapping("/sendMsg")
    public String sendMessage(@RequestParam("msg")String message){
        return WeChatBll.sendMessage("{\"touser\":\"oTX2Gv6HCMPkUkwJM0Cu6MpeHGTM\",\"msgtype\":\"text\",\"text\":{\"content\":\"" + message + "\"}}");
    }

    @ResponseBody
    @RequestMapping("/send")
    public String sendMessage(){
        return WeChatBll.sendMessage("{\"touser\":\"oTX2Gv6HCMPkUkwJM0Cu6MpeHGTM\",\"msgtype\":\"text\",\"text\":{\"content\":\"哈哈哈测试收件人的\"}}");
    }


    @RequestMapping("/wx")
    @ResponseBody
    public String login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String signature = httpServletRequest.getParameter("signature");
        String timestamp = httpServletRequest.getParameter("timestamp");
        String nonce = httpServletRequest.getParameter("nonce");
        String echostr = httpServletRequest.getParameter("echostr");
        if (CheckUntil.checkSignatures(signature, timestamp, nonce)) {
            return echostr;
        }
        return null;
    }
}
