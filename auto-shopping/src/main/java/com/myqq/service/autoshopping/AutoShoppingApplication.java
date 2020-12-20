package com.myqq.service.autoshopping;

import com.myqq.service.controller.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoShoppingApplication {


	public static void main(String[] args) {
		Class<?>[] primarySources = {OrderController.class, SampleController.class, WeChatController.class, OrderForAppController.class, OrderForWeChatAppController.class};
		SpringApplication.run(primarySources, args);
	}
}
