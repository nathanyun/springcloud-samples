package com.springcloud.circuitbreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.function.Supplier;

@Service
public class HelloService {

    @Autowired
    private RestTemplate restTemplate;

    // 调用某个服务的接口
    public Supplier<Map> helloSupplier(String name) {
        String url = "http://localhost:8080/hello?name=" + name;
        return () -> restTemplate.getForObject(url, Map.class);
    }

}
