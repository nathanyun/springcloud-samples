package com.springcloud.gateway;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

import java.util.function.Function;

@Configuration
public class GatewayConfig {


    /**
     * 通过编码方式配置路由
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){

        // 当访问 http://locahost:8080/china  时, 路由到 https://https://news.sina.com.cn/china
       return builder.routes().route("sina_china_route_rule", new Function<PredicateSpec, Buildable<Route>>() {
            @Override
            public Buildable<Route> apply(PredicateSpec predicate) {
                return predicate.path("/china").uri("https://news.sina.com.cn");
            }
        }).build();
    }

    // 解决 DNS 无法解析的问题
    @Bean
    public HttpClient webClient() {
        return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    }
}
