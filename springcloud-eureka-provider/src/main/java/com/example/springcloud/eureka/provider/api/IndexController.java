package com.example.springcloud.eureka.provider.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/index")
    public String hello(){
        System.err.println("---------hello---------run---------");
        return "Hello,world!";
    }
}
