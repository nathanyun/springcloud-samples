package com.springcloud.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;


/**
 * 断路器应用端
 * <pre>官方文档: {@link https://resilience4j.readme.io/docs/getting-started#section-modularization}</pre>
 */
@SpringBootApplication
public class Resilience4JCircuitBreakerApp {


    public static void main(String[] args) {
        SpringApplication.run(Resilience4JCircuitBreakerApp.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    // 断路器自定义配置
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {

        // 使用默认的断路器配置
        // CircuitBreakerConfig.ofDefaults();
        // 自定义断路器配置
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                // 以百分比配置失败率阈值。当失败率等于或大于阈值时，断路器状态 TODO 从关闭变为开启。
                .failureRateThreshold(50)
                // 当慢调用比例大于等于阈值时，TODO 从关闭变为开启。
                .slowCallRateThreshold(50)
                // 调用时间超过十秒认为是慢调用
                .slowCallDurationThreshold(Duration.ofSeconds(10))
                // 断路器在半开状态下允许通过的调用次数。
                .permittedNumberOfCallsInHalfOpenState(10)
                // 断路器在半开状态下的最长等待时间，超过该配置值的话，断路器会 TODO {从半开状态恢复为开启状态}。
                // 配置是0时表示断路器会一直处于半开状态，直到所有允许通过的访问结束。
                .maxWaitDurationInHalfOpenState(Duration.ofSeconds(30))
                // 断路器 TODO {从开启过渡到半开} 应等待的时间。
                .waitDurationInOpenState(Duration.ofSeconds(60))
                // 如果设置为true，则意味着断路器将自动从开启状态过渡到半开状态，并且不需要调用来触发转换。创建一个线程来监视断路器的所有实例，以便在WaitDurationInOpenstate之后将它们转换为半开状态。
                // 如果设置为false，则只有在发出调用时才会转换到半开，即使在waitDurationInOpenState之后也是如此。这里的优点是没有线程监视所有断路器的状态。
                .automaticTransitionFromOpenToHalfOpenEnabled(true)

                // 滑动窗口的类型，当断路器关闭时，将调用的结果记录在滑动窗口中。
                // 如果滑动窗口类型是COUNT_BASED，将会统计记录最近slidingWindowSize次调用的结果。
                // 如果是TIME_BASED，将会统计记录最近slidingWindowSize秒的调用结果。
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(100) // 滑动窗口大小
                // 断路器计算失败率或慢调用率之前所需的最小调用数（每个滑动窗口周期）。
                // 例如，如果minimumNumberOfCalls为10，则必须至少记录10个调用，然后才能计算失败率。
                // 如果只记录了9次调用，即使所有9次调用都失败，断路器也不会开启。
                .minimumNumberOfCalls(10)

                // 默认为空, 如果指定异常列表，则所有其他异常均视为成功，除非它们被ignoreExceptions显式忽略。
                .recordExceptions(IOException.class, TimeoutException.class)
                // 被忽略且既不算失败也不算成功的异常列表。
                // 优先级高于recordExceptions, 即使异常是recordExceptions的一部分, 也会忽略掉
                // .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                // 一个自定义断言，用于评估异常是否应记录为失败。
                // 如果异常应计为失败，则断言必须返回true。如果出断言返回false，应算作成功，除非ignoreExceptions显式忽略异常。
                //.recordException(e -> org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.equals( e.getMessage() ))
                .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                // 断路器配置
                .circuitBreakerConfig(circuitBreakerConfig)
                // 时间限流器配置
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3)).build())
                .build());
    }
}
