package com.myqq.service.controller;


import com.myqq.service.youza.util.CheckUntil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableAutoConfiguration
@RequestMapping("/wechat")
public class WeChatController {

    @ResponseBody
    @RequestMapping("/home")
    String home(){
        return "weChat Success!";
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
