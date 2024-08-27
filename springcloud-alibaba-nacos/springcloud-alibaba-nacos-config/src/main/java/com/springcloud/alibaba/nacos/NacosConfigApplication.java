package com.springcloud.alibaba.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 配置格式: ${prefix}-${spring.profiles.active}.${file-extension}
 * 例如: nacos-config-application-dev.yml, nacos-config-application-prod.yml
 */
@SpringBootApplication
public class NacosConfigApplication {

    public static void main(String[] args) {

        SpringApplication.run(NacosConfigApplication.class, args);
    }
}
