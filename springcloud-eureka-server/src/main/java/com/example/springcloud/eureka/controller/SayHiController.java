package com.example.springcloud.eureka.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SayHiController {

    @GetMapping("hi")
    public String hi(){
        System.err.println("Hello,Eureka!");
        return "Hello,Eureka!";
    }
}
