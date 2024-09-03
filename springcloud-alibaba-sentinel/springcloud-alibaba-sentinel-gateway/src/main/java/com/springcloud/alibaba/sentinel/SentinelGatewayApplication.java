package com.springcloud.alibaba.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.RedirectBlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 从 1.6.0 版本开始，Sentinel 提供了 Spring Cloud Gateway 的适配模块，可以提供两种资源维度的限流：
 *
 * <pre>
 *      - route 维度：即在 Spring 配置文件中配置的路由条目，资源名为对应的 routeId
 *      - 自定义 API 维度：用户可以利用 Sentinel 提供的 API 来自定义一些 API 分组
 * </pre>
 * https://github.com/alibaba/Sentinel/wiki/%E7%BD%91%E5%85%B3%E9%99%90%E6%B5%81#spring-cloud-gateway
 */
@EnableDiscoveryClient // 开启服务发现功能
@SpringBootApplication
public class SentinelGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelGatewayApplication.class, args);

        initBlockHandler();
        initGatewayRuleAPI();
    }


    @RestController
    static class GatewayController {

        @GetMapping("/product/get/{id}")
        public String hello(@PathVariable String id) {
            return "Get ==>" + id;
        }
    }

    /**
     * 初始化网关规则 API
     */
    private static void initGatewayRuleAPI() {

        //初始化API分组
        initCustomizedApis();

        Set<GatewayFlowRule> rules = new HashSet<>();
        /*
         * 结合文档和sentinel配置页面来写
         * 文档：https://github.com/alibaba/Sentinel/wiki/%E7%BD%91%E5%85%B3%E9%99%90%E6%B5%81
         */
        GatewayFlowRule rule = new GatewayFlowRule();
        // API资源类型 route: RESOURCE_MODE_ROUTE_ID 0, api分组：RESOURCE_MODE_CUSTOM_API_NAME 1
        rule.setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME);
        // 资源名称，可以是网关中的 route 名称或者用户自定义的 API 分组名称
        rule.setResource("sentinel-service-route-rule001");//TODO 测试后发现只有route 名称配置才生效
        // 阈值类型 QPS:FLOW_GRADE_QPS 1 线程数:FLOW_GRADE_THREAD 0
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // QPS阈值
        rule.setCount(1);
        // 统计时间窗口，单位是秒，默认是 1 秒
        rule.setIntervalSec(1);
        // 流量整形的控制效果，同限流规则的 controlBehavior 字段，目前支持快速失败CONTROL_BEHAVIOR_DEFAULT和匀速排队CONTROL_BEHAVIOR_RATE_LIMITER两种模式，默认是快速失败0。
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        // 应对突发请求时额外允许的请求数目，默认为int类型 0
        rule.setBurst(0);
        rules.add(rule);

        GatewayRuleManager.loadRules(rules);
    }


    /**
     * 自定义 api 分组
     */
    private static void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();

        ApiDefinition api1 = new ApiDefinition("product_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/product/get"));
                    add(new ApiPathPredicateItem().setPattern("/product/foo/**")
                            .setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_PREFIX));
                }});


        ApiDefinition api2 = new ApiDefinition("another_customized_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/echo"));
                }});

        definitions.add(api1);
        definitions.add(api2);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    /**
     * 初始化阻塞处理器
     */
    private static void initBlockHandler() {
        // 自定义异常结果
        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                Map<String, Object> map = new HashMap<>();
                map.put("uri", serverWebExchange.getRequest().getURI());
                map.put("msg","访问量过大，稍后请重试");
                map.put("code", HttpStatus.TOO_MANY_REQUESTS.value());

                return ServerResponse
                        .status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(map), Map.class);
            }
        });

        // 也可以设置重定向页面, 例如:
        //GatewayCallbackManager.setBlockHandler(new RedirectBlockRequestHandler("https://baidu.com"));
    }

}
