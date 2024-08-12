package com.springcloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient // 启动服务发现客户端
@SpringBootApplication
public class ConfigClientApp {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApp.class, args);
	}

}
