package com.springcloud.alibaba.sentinel;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@EnableDiscoveryClient // 开启服务发现功能
@SpringBootApplication
public class SentinelDataSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelDataSourceApplication.class, args);
    }

    // 支持对 RestTemplate 的服务调用使用 Sentinel 进行保护
    // 通常用于服务间的调用，保护远程服务调用不受流量过载的影响
    @SentinelRestTemplate(fallback = "fallbackA", fallbackClass = MyBlockHandler.class, blockHandler = "blockA", blockHandlerClass = MyBlockHandler.class)
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @RestController
    static class TestController{
        private final RestTemplate restTemplate;

        public TestController(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
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
         * 慢调用
         * @return
         */
        @GetMapping(value = "/hello")
        public String hello() {
            return restTemplate.getForObject("http://sentinel-service-provider/hello", String.class);
        }

    }
}
