package com.myqq.service.controller;

import com.myqq.service.youza.bll.WeChatBll;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

import static com.myqq.service.youza.constinfo.ConstInfoForApp.qqOpenId;
import static com.myqq.service.youza.constinfo.ConstInfoForApp.zzjjOpenId;

@Controller
@EnableAutoConfiguration
@RequestMapping("/sample")
public class SampleController {

    @ResponseBody
    @RequestMapping("/home")
    String home(){
        return "Sample Success!";
    }

    @ResponseBody
    @RequestMapping("/sendMsg")
    public String sendMessage(@RequestParam("msg")String message){
        return WeChatBll.sendMessage("{\"touser\":\"oTX2Gv6HCMPkUkwJM0Cu6MpeHGTM\",\"msgtype\":\"text\",\"text\":{\"content\":\"" + message + "\"}}", Arrays.asList(qqOpenId));
    }
}
