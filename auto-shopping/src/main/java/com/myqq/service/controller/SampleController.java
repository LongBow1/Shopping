package com.myqq.service.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
@RequestMapping("/sample")
public class SampleController {

    @ResponseBody
    @RequestMapping("/home")
    String home(){
        return "Sample Success!";
    }

}
