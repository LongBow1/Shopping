package com.myqq.service.autoshopping;

import com.myqq.service.controller.OrderController;
import com.myqq.service.controller.OrderForAppController;
import com.myqq.service.controller.SampleController;
import com.myqq.service.controller.WeChatController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AutoShoppingApplication {

	public static void main(String[] args) {
		Class<?>[] primarySources = {OrderController.class, SampleController.class, WeChatController.class, OrderForAppController.class};
		SpringApplication.run(primarySources, args);
	}
}
