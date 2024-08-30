package com.springcloud.alibaba.nacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/config")
@RefreshScope // 实现配置自动更新
public class ConfigController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @Autowired
    private Environment environment;

    @RequestMapping("/get")
    public Map<String, Object> get() {
        Map<String, Object> config = new HashMap<>();
        config.put("useLocalCache", useLocalCache);
        config.put("activeProfiles", environment.getActiveProfiles());
        config.put("defaultProfiles", environment.getDefaultProfiles());
        config.put("spring.profiles.active", environment.getProperty("spring.profiles.active"));
        config.put("spring.application.name", environment.getProperty("spring.application.name"));
        return config;
    }
}
