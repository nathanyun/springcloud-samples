package com.springcloud.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 服务降级测试, 访问这个接口, 模拟接口异常, 让客户端感知到, 触发网关的断路器重定向到fallback
 */
@Slf4j
@RestController
public class DowngradeController {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private int serverPort;

    @RequestMapping(value = "downgrade")
    public String downgrade(){
        String message = String.format("downgrade executed! appName: %s, serverPort: %d", appName, serverPort);
        log.warn(message);
        //throw new RuntimeException(message);
        return message;
    }
}
