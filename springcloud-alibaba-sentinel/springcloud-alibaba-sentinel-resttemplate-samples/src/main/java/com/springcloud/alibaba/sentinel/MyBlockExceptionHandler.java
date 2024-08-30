package com.springcloud.alibaba.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.Serializable;

/**
 * 自定义异常限流处理, 若没有自定义就会走默认的实现类 {@link com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.DefaultBlockExceptionHandler}
 */
@Slf4j
public class MyBlockExceptionHandler implements BlockExceptionHandler {


    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        log.info(".....................................MyBlockHandler.....................................start");
        RestObject restObject = null;

        // 不同的异常返回不同的提示语
        if (e instanceof FlowException) {
            restObject = RestObject.builder().statusCode(100).statusMessage("接口限流了").build();

        } else if (e instanceof DegradeException) {
            restObject = RestObject.builder().statusCode(101).statusMessage("服务降级了").build();

        } else if (e instanceof ParamFlowException) {
            restObject = RestObject.builder().statusCode(102).statusMessage("热点参数限流了").build();

        } else if (e instanceof SystemBlockException) {
            restObject = RestObject.builder().statusCode(103).statusMessage("触发系统保护规则").build();

        } else if (e instanceof AuthorityException) {
            restObject = RestObject.builder().statusCode(104).statusMessage("授权规则不通过").build();
        }

        //返回json数据
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //springmvc 的一个json转换类 （jackson）
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), restObject);

        //跳转
        //request.getRequestDispatcher("/index.jsp").forward(request, response);

        //重定向
        //response.sendRedirect("http://www.baidu.com");
    }

    @Setter
    @Builder
    static class RestObject implements Serializable {
        private Integer statusCode;
        private String statusMessage;
        private String characterEncoding;
        private String contentType;
    }
}
