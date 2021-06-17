package com.example.springcloud.eureka.server.second;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer // 启用服务发现，注册中心
@SpringBootApplication // springboot 核心配置
public class EurekaServerSecondApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerSecondApplication.class, args);
    }

}
