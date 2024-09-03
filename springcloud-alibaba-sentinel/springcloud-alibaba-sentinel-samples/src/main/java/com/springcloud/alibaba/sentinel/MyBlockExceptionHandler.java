package com.springcloud.alibaba.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.net.URI;

/**
 * 自定义异常限流处理, 若没有自定义就会走默认的实现类 {@link com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.DefaultBlockExceptionHandler}
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class MyBlockExceptionHandler implements BlockExceptionHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        URI uri = URI.create(request.getRequestURI());

        log.info(".....................................MyBlockHandler.....................................start uri: {}, error : {}", uri, e.getRule());

        RestObject restObject;

        // 不同的异常返回不同的提示语
        if (e instanceof FlowException) {
            restObject = RestObject.builder().statusCode(HttpStatus.TOO_MANY_REQUESTS.value()).statusMessage("接口限流了").build();

        } else if (e instanceof DegradeException) {
            restObject = RestObject.builder().statusCode(HttpStatus.SERVICE_UNAVAILABLE.value()).statusMessage("服务降级了").build();

        } else if (e instanceof ParamFlowException) {
            restObject = RestObject.builder().statusCode(HttpStatus.PRECONDITION_FAILED.value()).statusMessage("热点参数限流了").build();

        } else if (e instanceof SystemBlockException) {
            restObject = RestObject.builder().statusCode(HttpStatus.NOT_ACCEPTABLE.value()).statusMessage("触发系统保护规则").build();

        } else if (e instanceof AuthorityException) {
            restObject = RestObject.builder().statusCode(HttpStatus.UNAUTHORIZED.value()).statusMessage("授权规则不通过").build();

        } else {
            restObject = RestObject.builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).statusMessage("系统繁忙").build();
        }

        //返回json数据
        response.setStatus(restObject.getStatusCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.print(JSON.toJSONString(restObject));
        out.flush();
        out.close();

        //跳转
        //request.getRequestDispatcher("/index.jsp").forward(request, response);

        //重定向
        //response.sendRedirect("http://www.baidu.com");
    }

}
