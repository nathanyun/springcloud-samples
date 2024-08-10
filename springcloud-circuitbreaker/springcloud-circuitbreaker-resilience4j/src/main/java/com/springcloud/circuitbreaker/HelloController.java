package com.springcloud.circuitbreaker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class HelloController {

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello/{name}")
    public Map hello(@PathVariable String name) {
        // 使用断路器调用服务, 若服务提供者异常, 可自动检测&自动恢复
        return circuitBreakerFactory.create("helloabcdefg").run(helloService.helloSupplier(name), t -> {
            log.warn("hello call failed error", t);
            // 若调用失败, 则返回默认值
            // 可通过手动停止服务提供者来测试 fallback
            Map<String, String> fallback = new HashMap<>();
            fallback.put("hello", "world");
            fallback.put("remark", "This is fallback message");
            return fallback;
        });
    }
}
