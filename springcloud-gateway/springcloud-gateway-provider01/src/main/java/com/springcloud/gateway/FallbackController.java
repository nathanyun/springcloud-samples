package com.springcloud.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @Value("${spring.application.name}")
    private String appName;

    @RequestMapping("fallback")
    public String fallback(){
        return String.format("Fallback already by: %s", appName);
    }
}
