package com.springcloud.alibaba.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@LoadBalancerClient(name = "service-consumer", configuration = MyLoadBalancerConfig.class)
@EnableDiscoveryClient // 开启服务发现功能
@SpringBootApplication
public class ServiceConsumerApplication {

    public static void main(String[] args) {

        SpringApplication.run(ServiceConsumerApplication.class, args);
    }

    @Bean
    @LoadBalanced  //使用这个注解给restTemplate赋予了负载均衡的能力
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
