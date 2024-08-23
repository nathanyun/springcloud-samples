package com.springcloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication // springboot 核心配置
public class CloudConfigEurekaServerApp {

    public static void main(String[] args) {
        SpringApplication.run(CloudConfigEurekaServerApp.class, args);
    }

}
