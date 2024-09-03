package com.springcloud.alibaba.sentinel;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;


/**
 * 自定义限流处理方法
 */
@Slf4j
public class MyBlockHandler {

    /**
     * 限流后处理方法
     * <pre>这里可以处理很多自定义逻辑, 例如返回默认信息, 跳转错误页, 发送邮件, 或者执行特定逻辑等等</pre>
     */
    public static ClientHttpResponse blockA(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException ex) {

        String message = RestObject.builder()
                .requestURI(request.getURI())
                .statusCode(500)
                .statusMessage("接口限流了")
                .build().toString();

        log.error("blockA --- {} : {}" , message, ex.getMessage());
        return new SentinelClientHttpResponse(message);
    }

    /**
     * 熔断后处理的方法
     */
    public static ClientHttpResponse fallbackA(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException ex) {

        String message = RestObject.builder()
                .requestURI(request.getURI())
                .statusCode(503)
                .statusMessage("系统降级了")
                .build().toString();

        log.error("fallbackA --- {} : {}" , message, ex.getMessage());
        return new SentinelClientHttpResponse(message);
    }
}
