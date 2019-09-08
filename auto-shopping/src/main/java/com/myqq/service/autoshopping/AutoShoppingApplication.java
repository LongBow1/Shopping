package com.myqq.service.autoshopping;

import com.myqq.service.controller.OrderController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AutoShoppingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderController.class, args);
	}
}
