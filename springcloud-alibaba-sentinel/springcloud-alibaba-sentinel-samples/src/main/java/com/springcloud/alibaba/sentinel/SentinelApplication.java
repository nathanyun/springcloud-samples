package com.springcloud.alibaba.sentinel;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.springcloud.alibaba.sentinel.openfeign.EchoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


// 这个 name 必须和 application.yaml 中的 spring.application.name 保持一致
@LoadBalancerClient(name = "sentinel-app1", configuration = MyLoadBalancerConfig.class)
@EnableDiscoveryClient // 开启服务发现功能
@EnableFeignClients // 这个注解是通知SpringBoot在启动的时候，扫描被 @FeignClient 修饰的类，@FeignClient这个注解在进行远程调用的时候会用到。
@SpringBootApplication
public class SentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelApplication.class, args);
    }

    // 支持对 RestTemplate 的服务调用使用 Sentinel 进行保护
    // 通常用于服务间的调用，保护远程服务调用不受流量过载的影响
    @SentinelRestTemplate(fallback = "fallbackA", fallbackClass = MyBlockHandler.class, blockHandler = "blockA", blockHandlerClass = MyBlockHandler.class)
    @LoadBalanced  //使用这个注解给restTemplate赋予了负载均衡的能力
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @RestController
    static class TestController{
        private final RestTemplate restTemplate;
        private final EchoService echoService;

        public TestController(RestTemplate restTemplate, EchoService echoService) {
            this.restTemplate = restTemplate;
            this.echoService = echoService;
        }

        /**
         *  @SentinelRestTemplate 方式熔断和限流案例, 是对 RestTemplate 这种服务间调用的客户端保护
         * @param str
         * @return
         */
        @GetMapping(value = "/echo/{str}")
        public String echo(@PathVariable String str) {
            // 通过 restTemplate 调用 服务提供者的接口
            // 其中 sentinel-service-provider 是服务名
            return restTemplate.getForObject(String.format("http://sentinel-service-provider/echo/%s", str), String.class);
        }

        /**
         *  @FeignClient 方式熔断和限流案例
         * @param str
         * @return
         */
        @GetMapping(value = "/echo2/{str}")
        public String echoOpenFeign(@PathVariable String str) {
            // 通过 openFeign 调用 服务提供者的接口
            return echoService.echo(str);
        }

        /**
         * @SentinelResource 注解用来标识资源是否被限流、降级, 一般用于方法级别的流量控
         * @param name
         * @return
         */
        @GetMapping("hello/{name}")
        @SentinelResource(fallback = "fallbackA", fallbackClass = MyBlockHandler.class, blockHandler = "blockA", blockHandlerClass = MyBlockHandler.class)
        public String hello(@PathVariable String name) {
            return "hello " + name;
        }
    }
}
