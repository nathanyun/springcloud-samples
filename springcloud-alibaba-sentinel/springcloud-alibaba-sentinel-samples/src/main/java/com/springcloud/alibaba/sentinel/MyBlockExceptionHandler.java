package com.springcloud.alibaba.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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

        log.info(".....................................MyBlockHandler.....................................start uri: {}, error : {}", uri, e.getMessage());

        //返回json数据
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.print("{\"code\": 429, \"msg\": \"请求过于频繁，请稍后再试\"}");
        out.flush();
        out.close();

        //跳转
        //request.getRequestDispatcher("/index.jsp").forward(request, response);

        //重定向
        //response.sendRedirect("http://www.baidu.com");
    }

}
