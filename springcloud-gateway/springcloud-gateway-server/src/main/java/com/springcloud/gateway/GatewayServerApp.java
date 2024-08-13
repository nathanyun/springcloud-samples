package com.springcloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 服务提供者,对外暴露一个 HTTP接口, 供调用方使用
 */
@EnableDiscoveryClient// 新版本已经废弃了@EnableEurekaClient, 改用@EnableDiscoveryClient注解
@SpringBootApplication
public class GatewayServerApp {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApp.class, args);
	}

}
