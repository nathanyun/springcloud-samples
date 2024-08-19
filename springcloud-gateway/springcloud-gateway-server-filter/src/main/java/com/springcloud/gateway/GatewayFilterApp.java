package com.springcloud.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient// 新版本已经废弃了@EnableEurekaClient, 改用@EnableDiscoveryClient注解
@SpringBootApplication
public class GatewayFilterApp {

	public static void main(String[] args) {
		SpringApplication.run(GatewayFilterApp.class, args);
	}


	// 断路器自定义配置
	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
		// 使用默认的断路器配置
		CircuitBreakerConfig defaultsCircuitBreakerConfig = CircuitBreakerConfig.ofDefaults();
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				// 断路器配置
				.circuitBreakerConfig(defaultsCircuitBreakerConfig)
				.build());
	}
}
