package com.springcloud.alibaba.sentinel;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    private final RestTemplate restTemplate;

    public TestController(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

    @GetMapping(value = "/echo/{str}")
    public String echo(@PathVariable String str) {
        // 通过 restTemplate 调用 服务提供者的接口
        // 其中 sentinel-service-provider 是服务名
        return restTemplate.getForObject(String.format("http://sentinel-service-provider/echo/%s", str), String.class);
    }
}
