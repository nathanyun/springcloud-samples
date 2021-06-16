package com.example.springcloud.eureka.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("getByUrl")
    public String getByUrl(){
        // 因为@Autowired的RestTemplate已经加了@LoadBalanced注解，具有了服务发现的特性，无法进行URL调用，所以这里用自己创建一个来调用。
        ResponseEntity<String> responseEntity = new RestTemplate().getForEntity("http://127.0.0.1:7001/index", String.class);
        System.err.println("getByUrl 服务调用结果="+ responseEntity.getBody());
        return "getByUrl 服务调用结果="+responseEntity.getBody();
    }

    @GetMapping("getByServiceName")
    public String getByServiceName(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://provider-service/index", String.class);
        System.err.println("getByServiceName 服务调用结果="+ responseEntity.getBody());
        return "getByServiceName 服务调用结果="+responseEntity.getBody();
    }

}
