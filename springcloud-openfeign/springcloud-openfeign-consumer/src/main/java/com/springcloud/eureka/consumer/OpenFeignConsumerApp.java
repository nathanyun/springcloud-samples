package com.springcloud.eureka.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableFeignClients //  这个注解是通知SpringBoot在启动的时候，扫描被 @FeignClient 修饰的类，@FeignClient这个注解在进行远程调用的时候会用到。
@EnableDiscoveryClient
@SpringBootApplication
public class OpenFeignConsumerApp {

    @Autowired
    private HelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(OpenFeignConsumerApp.class, args);
    }

    @RequestMapping("/hello/{name}")
    public String index(@PathVariable("name") String name) {
        return "OpenFeignConsumerApp remote call result ==> " + helloService.hello(name);
    }


    /**
     * 远程服务的接口定义
     * <pre>name = "openfeign-producer-app" 远程服务名，即: spring.application.name配置的名称</pre>
     */
    @FeignClient(name = "openfeign-producer-app")
    public interface HelloService {

        /**
         * 远程服务的接口
         * <pre>方法和远程服务中controller中的方法名和参数需保持一致</pre>
         * @param name
         * @return
         */
        @RequestMapping(value = "/hello")
        String hello(@RequestParam(value = "name") String name);
    }
}
