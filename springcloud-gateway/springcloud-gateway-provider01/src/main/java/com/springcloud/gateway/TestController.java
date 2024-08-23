package com.springcloud.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private int serverPort;

    @RequestMapping(value = "test")
    public String multiPredicatesRoute(@RequestParam(value = "name") String name){
        log.info("test name : {}", name);
        return String.format("Hello, %s ! Power by appName: %s, serverPort: %d", name, appName, serverPort);
    }
}