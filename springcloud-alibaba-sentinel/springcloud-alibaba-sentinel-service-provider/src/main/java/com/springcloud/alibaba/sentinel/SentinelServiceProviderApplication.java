package com.springcloud.alibaba.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


@EnableDiscoveryClient // 开启服务发现功能
@SpringBootApplication
public class SentinelServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelServiceProviderApplication.class, args);
    }


    // 对外提供一个回声测试的接口
    @RestController
    static class EchoController {
        @GetMapping(value = "/echo/{string}")
        public String echo(@PathVariable String string) {
            return "Hello Sentinel Service " + string;
        }

        @GetMapping(value = "/hello")
        public String hello(@RequestParam(value = "name", defaultValue = " World") String name) {
            try {
                TimeUnit.MILLISECONDS.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Hello " + name;
        }
    }
}
