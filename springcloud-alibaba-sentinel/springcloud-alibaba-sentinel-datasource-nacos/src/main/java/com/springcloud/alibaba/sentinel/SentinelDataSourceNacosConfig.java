package com.springcloud.alibaba.sentinel;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <pre>
 * 经测试, 貌似也无需写这个配置类, 只要在 application.yml 中配置了{@code       datasource:
 *         flow-control: # 流控管理（这个名称可以自定义）
 *           nacos:
 *             server-addr: ${spring.cloud.nacos.config.server-addr}
 *             data-id: ${spring.application.name}-flow-rules.json
 *             data-type: json
 *             rule-type: flow}  nacos 中修改配置, 应用就会自动监听到
 * </pre>
 *
 */
@Slf4j
//@Component
public class SentinelDataSourceNacosConfig {
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;
    @Value("${spring.cloud.nacos.config.group}")
    private String groupId;
    @Value("${spring.application.name}")
    private String applicationName;

    @PostConstruct
    private void init() {
        // 系统初始化时加载配置文件, 定时轮询拉取新配置
        ReadableDataSource<String, List<FlowRule>> flowRule = new NacosDataSource<>(serverAddr, groupId, applicationName + "-flow-rules.json",
                source -> {
                    if (!StringUtils.hasLength(source)) {
                        return new ArrayList<>();
                    }
                    return JSON.parseObject(source, new TypeReference<>() {
                    });
                });
        log.info("FlowRuleManager load flowRule: {}", JSON.toJSONString(flowRule.getProperty(), true));
        FlowRuleManager.register2Property(flowRule.getProperty());

        // 注册nacos中的降级规则到 sentinel中
        ReadableDataSource<String, List<DegradeRule>> degradeRule = new NacosDataSource<>(serverAddr, groupId, applicationName + "-degrade-rules.json", source -> JSON.parseObject(source, new TypeReference<>() {}));
        log.info("DegradeRuleManager load degradeRule: {}", JSON.toJSONString(degradeRule.getProperty(), true));
        DegradeRuleManager.register2Property(degradeRule.getProperty());
    }


    /*
    sentinel-datasource-application-flow-rules.json 配置如下:
    [
      {
        "resource": "/echo", // 资源名
        "limitApp": "default", // // 针对来源，若为 default 则不区分调用来源
        "grade": 1, // 限流阈值类型(1:QPS;  0:并发线程数）
        "count": 1, // 阈值
        "clusterMode": false, // 是否是集群模式
        "controlBehavior": 0, // 流控效果 (0:快速失败;  1:Warm Up(预热模式);  2:排队等待)
        "strategy": 0,  // 流控模式(0:直接； 1:关联; 2:链路)
        "warmUpPeriodSec": 10, // 预热时间（秒，预热模式需要此参数）
        "maxQueueingTimeMs": 500, // 超时时间（排队等待模式需要此参数）
        "refResource": "rrr" // 关联资源、入口资源(关联、链路模式)
      }
    ]


    sentinel-datasource-application-degrade-rules.json 配置如下:
    [
        {
            "resource": "/degrade",
            "grade": 0, // 熔断策略，支持慢调用比例（0），异常比例（1），异常数（2）策略
            "count": 1000, // 慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用，单位ms）；异常比例/异常数模式下为对应的阈值
            "slowRatioThreshold": 0.1,// 慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）
            "minRequestAmount":  10, //熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
            "timeWindow": 10, // 熔断时长，单位为 s
            "statIntervalMs": 1000 // 统计时长（单位为 ms），如 60*1000 代表分钟级
        }
    ]

    */
}
