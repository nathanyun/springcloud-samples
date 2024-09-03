package com.springcloud.alibaba.sentinel.openfeign;

import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    public EchoServiceFallback echoServiceFallback() {
        return new EchoServiceFallback();
    }
}
