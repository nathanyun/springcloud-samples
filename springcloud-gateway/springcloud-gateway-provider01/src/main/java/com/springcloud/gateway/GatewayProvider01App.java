package com.springcloud.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务提供者,对外暴露一个 HTTP接口, 供调用方使用
 */
@Slf4j
@EnableDiscoveryClient// 新版本已经废弃了@EnableEurekaClient, 改用@EnableDiscoveryClient注解
@SpringBootApplication
@RestController
public class GatewayProvider01App {

	@Value("${spring.application.name}")
	private String appName;

	@Value("${server.port}")
	private int serverPort;

	/**
	 * 对外提供一个接口
	 * @return
	 */
	@RequestMapping(value = "p1/hello")
	public String hello(@RequestParam(value = "name") String name){
		log.info("hello name : {}", name);
		return String.format("Hello, %s ! Power by appName: %s, serverPort: %d", name, appName, serverPort);
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayProvider01App.class, args);
	}

}
