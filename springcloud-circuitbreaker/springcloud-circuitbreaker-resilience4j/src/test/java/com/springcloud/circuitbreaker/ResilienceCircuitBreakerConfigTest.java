package com.springcloud.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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


        // 处理CircuitBreakerEvent事件
        // CircuitBreakerEvent可以是状态转换、断路器重置、成功调用、记录的错误或被忽略的错误。
        // 所有事件都包含其他信息，如事件创建时间和调用的处理持续时间。
        // 如果要使用事件，则必须注册事件使用者
        circuitBreaker.getEventPublisher()
                .onSuccess(event -> log.info("onSuccess"))
                .onError(event -> log.info("onError"))
                .onIgnoredError(event -> log.info("onIgnoredError"))
                .onReset(event -> log.info("onReset"))
                .onStateTransition(event -> log.info("onStateTransition"))
                // 如果你想对所有事件的进行监听并处理，可以这样做
                .onEvent(event -> log.info("onEvent"));
    }
}
