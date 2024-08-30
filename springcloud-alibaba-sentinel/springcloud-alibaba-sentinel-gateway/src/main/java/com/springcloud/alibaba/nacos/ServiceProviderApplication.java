package com.springcloud.alibaba.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@EnableDiscoveryClient // 开启服务发现功能
@SpringBootApplication
public class ServiceProviderApplication {

    public static void main(String[] args) {

        SpringApplication.run(ServiceProviderApplication.class, args);
    }


    // 对外提供一个回声测试的接口
    @RestController
    static class EchoController {
        @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
        public String echo(@PathVariable String string) {
            return "Hello Nacos Discovery " + string;
        }
    }
}
