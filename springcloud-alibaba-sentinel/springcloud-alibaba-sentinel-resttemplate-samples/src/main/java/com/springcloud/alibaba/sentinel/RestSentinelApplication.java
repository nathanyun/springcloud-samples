package com.springcloud.alibaba.sentinel;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


// 这个 name 必须和 application.yaml 中的 spring.application.name 保持一致
@LoadBalancerClient(name = "sentinel-app1", configuration = MyLoadBalancerConfig.class)
@EnableDiscoveryClient // 开启服务发现功能
@SpringBootApplication
public class RestSentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestSentinelApplication.class, args);
    }

    // 支持对 RestTemplate 的服务调用使用 Sentinel 进行保护
    //@SentinelRestTemplate(blockHandler = "handle", blockHandlerClass = MyBlockExceptionHandler.class)
    @LoadBalanced  //使用这个注解给restTemplate赋予了负载均衡的能力
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
