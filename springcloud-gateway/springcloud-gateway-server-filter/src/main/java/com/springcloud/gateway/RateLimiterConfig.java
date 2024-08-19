package com.springcloud.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    /**
     * 用户key 解析器
     * @return
     */
    @Bean(name = "userKeyResolver")
    KeyResolver userKeyResolver(){

        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {

                ServerHttpRequest request = exchange.getRequest();
                MultiValueMap<String, String> queryParams = request.getQueryParams();
                String userId = queryParams.getFirst("userId");
                return Mono.justOrEmpty(userId);
            }
        };
    }

    /**
     * 请求号限流解析器
     * @return
     */
    @Primary
    @Bean(name = "requestIdKeyResolver")
    KeyResolver requestIdKeyResolver(){

        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {

                ServerHttpRequest request = exchange.getRequest();
                HttpHeaders headers = request.getHeaders();
                return Mono.justOrEmpty(headers.getFirst("X-Request-ID"));
            }
        };
    }

}
