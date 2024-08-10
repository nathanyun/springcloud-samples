package com.springcloud.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

public class ResilienceCircuitBreakerConfigTest {


    public static void main(String[] args) {
        // 使用 CircuitBreakerRegistry 创建 CircuitBreakerConfig
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(70)
                .build();

        // 使用默认配置创建circuitBreakerRegistry
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();

        // 使用circuitBreakerRegistry创建断路器，配置是默认配置
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("name1");

        // 使用circuitBreakerConfig创建注册器，进而创建断路器，配置是自定义配置
        CircuitBreaker circuitBreakerWithCustomConfig = CircuitBreakerRegistry.of(circuitBreakerConfig).circuitBreaker("diy");


    }
}
