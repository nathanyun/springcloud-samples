package com.springcloud.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;


@SpringBootTest
@AutoConfigureWebTestClient// 因为这个项目是spring-cloud-gateway 基于 reactive 构建的,无法使用 MockMvc 测试(mockMvc 需引入spring-boot-starter-web 依赖)
public class SpringCloudGatewayLimiterTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testHeaderRouteLimiter() throws Exception {

        // 请求多次
        for (int i = 0; i < 2; i++) {

            String responseBody = webTestClient.get().uri("http://localhost:8080/p1/hello?name=jack").header("X-Request-Id", "123456") // 添加 header
                    .exchange() // 发送请求
                    .expectStatus().isOk()// 断言响应状态为200
                    .expectBody(String.class) // 断言响应体为字符串
                    .returnResult().getResponseBody(); // 获取结果
            System.out.println("第"+i+"次, responseBody = " + responseBody);
        }
    }

}
