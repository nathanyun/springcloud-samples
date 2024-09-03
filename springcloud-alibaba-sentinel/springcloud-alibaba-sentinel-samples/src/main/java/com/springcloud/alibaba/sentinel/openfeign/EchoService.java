package com.springcloud.alibaba.sentinel.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 对应的资源名为 GET:http://sentinel-service-provider/echo/{str}。
 */
@FeignClient(name = "sentinel-service-provider", fallback = EchoServiceFallback.class, configuration = FeignConfig.class)
public interface EchoService {

    @GetMapping("/echo/{str}")
    String echo(@PathVariable String str);
}
