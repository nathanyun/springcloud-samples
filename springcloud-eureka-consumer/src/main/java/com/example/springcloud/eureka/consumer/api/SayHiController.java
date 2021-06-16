package com.example.springcloud.eureka.consumer.api;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SayHiController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("hi")
    public String hi(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http:eureka-provider:8903/index", String.class);
        System.err.println("RestTemplate调用接口获取数据....responseEntity="+ JSON.toJSONString(responseEntity,true));
        return responseEntity.getBody();
    }
}
