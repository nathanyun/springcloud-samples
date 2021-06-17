package com.example.spingcloud.eureka.server.third;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerThirdApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerThirdApplication.class, args);
    }

}
