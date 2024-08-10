package com.springcloud.circuitbreaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务提供者,对外暴露一个 HTTP接口, 供调用方使用
 */
@EnableDiscoveryClient// 新版本已经废弃了@EnableEurekaClient, 改用@EnableDiscoveryClient注解
@SpringBootApplication
@RestController
public class CircuitBreakerProducerApp {

	@RequestMapping("/")
	public String home(){
		return "Welcome, Current time :" +System.currentTimeMillis();
	}

	/**
	 * 对外提供一个接口
	 * @return
	 */
	@RequestMapping(value = "/hello")
	public Map hello(@RequestParam(value = "name") String name){
		Map<Object, Object> data = new HashMap<>();
		data.put("hello", name);
		data.put("remark", "我是一个服务提供者返回的信息");
		data.put("time", System.currentTimeMillis());
		return data;
	}

	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerProducerApp.class, args);
	}

}
