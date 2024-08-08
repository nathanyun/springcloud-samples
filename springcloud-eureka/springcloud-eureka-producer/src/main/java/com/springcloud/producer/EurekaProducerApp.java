package com.springcloud.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 新版本已经废弃了@EnableEurekaClient, 改用@EnableDiscoveryClient注解
@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class EurekaProducerApp {

	@RequestMapping("/")
	public String home(){
		return "Welcome";
	}

	public static void main(String[] args) {
		SpringApplication.run(EurekaProducerApp.class, args);
	}

}
