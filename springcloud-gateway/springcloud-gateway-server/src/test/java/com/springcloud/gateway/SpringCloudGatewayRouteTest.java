package com.springcloud.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;


@SpringBootTest
@AutoConfigureWebTestClient// 因为这个项目是spring-cloud-gateway 基于 reactive 构建的,无法使用 MockMvc 测试(mockMvc 需引入spring-boot-starter-web 依赖)
public class SpringCloudGatewayRouteTest {

    @Autowired
    private WebTestClient webTestClient;

    /**
     * 测试基于cookie路由
     *
     * <pre>
     *  - 配置如下:  {@code
     * spring:
     *   cloud:
     *     gateway:
     *       routes:
     *         - id: gateway-provider02-service-route-cookie
     *           uri: http://localhost:8082
     *           # 断言cookie的sessionId值为test时, 路由到 http://localhost:8082 的服务
     *           predicates:
     *             - Cookie=sessionId, test
     *     }
     * </pre>
     * @throws Exception
     *
     */
    @Test
    public void testCookieRoute() throws Exception {

        // 执行模拟的HTTP请求
        String responseBody = webTestClient.get().uri("http://localhost:8080/getAppName").cookie("sessionId", "test") // 添加cookie
                .exchange() // 发送请求
                .expectStatus().isOk()// 断言响应状态为200
                .expectBody(String.class) // 断言响应体为字符串
                .returnResult().getResponseBody(); // 获取结果
        System.out.println("responseBody = " + responseBody);

        // 结果 Route matched: gateway-provider02-service-route-cookie
        // Mapping [Exchange: GET http://localhost:8080/getAppName] to Route{id='gateway-provider02-service-route-cookie', uri=http://localhost:8082, order=0, predicate=Cookie: name=sessionId regexp=test, gatewayFilters=[], metadata={}}
    }

    /**
     * 测试基于 header 路由
     *
     * <pre>
     *  - 配置如下:  {@code
     * spring:
     *   cloud:
     *     gateway:
     *       routes:
     *         - id: gateway-provider02-service-route-header
     *           uri: http://localhost:8082
     *           predicates:
     *             - - Header=X-Request-Id, \d+
     *     }
     * </pre>
     * @throws Exception
     *
     */
    @Test
    public void testHeaderRoute() throws Exception {

        // 执行模拟的HTTP请求
        String responseBody = webTestClient.get().uri("http://localhost:8080/getAppName").header("X-Request-Id", "123456") // 添加 header
                .exchange() // 发送请求
                .expectStatus().isOk()// 断言响应状态为200
                .expectBody(String.class) // 断言响应体为字符串
                .returnResult().getResponseBody(); // 获取结果
        System.out.println("responseBody = " + responseBody);

        // 结果 Route matched: gateway-provider02-service-route-header
        // Mapping [Exchange: GET http://localhost:8080/getAppName] to Route{id='gateway-provider02-service-route-header', uri=http://localhost:8082, order=0, predicate=Header: X-Request-Id regexp=\d+, gatewayFilters=[], metadata={}}
    }

    /**
     * 测试基于 host 路由
     * <pre>当请求http://localhost:8080/f?kw=dnf 会通过host路由到 https://tieba.baidu.com:443/f?kw=dnf</pre>
     * @throws Exception
     */
    @Test
    public void testHostRoute() throws Exception {
        // 因为百度返回的响应是一个较大的网页, 会超过spring的最大内存限制, 具体错误如下:
        // org.springframework.core.io.buffer.DataBufferLimitException: Exceeded limit on max bytes to buffer : 262144
        // 可通过修改spring.codec.max-in-memory-size=500KB 来解决,但是不推荐这么做, 建议流式处理, 代码如下:

        Flux<String> htmlContent = webTestClient.get().uri("http://localhost:8080/f?kw=dnf")
                .header("Host", "anyway.baidu.com") // 使用Host头指定host
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

        // 流式处理, 第一个参数是打印内容, 第二个参数为处理异常
        htmlContent.subscribe(s -> System.out.println("contentOfPart = " + s), throwable -> System.err.println(throwable.getMessage()));
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * 测试基于 method 路由
     * @throws Exception
     */
    @Test
    public void testMethodRoute() throws Exception {
        Flux<String> htmlContent = webTestClient.get().uri("http://localhost:8080/f?kw=dnf")
                //.accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

        htmlContent.subscribe(System.out::println, e -> System.err.println(e.getMessage()));
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * 测试基于 查询参数正则匹配 路由
     * @throws Exception
     */
    @Test
    public void testQueryRoute() throws Exception {
        Flux<String> htmlContent = webTestClient.get().uri("http://localhost:8080/f?kw=lolm")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

        htmlContent.subscribe(System.out::println, e -> System.err.println(e.getMessage()));
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * 测试基于 IP 路由
     * @throws Exception
     */
    @Test
    public void testRemoteAddrRoute() throws Exception {
        Flux<String> htmlContent = webTestClient.get().uri("http://127.0.0.1:8080/ent")
                .header("X-Forwarded-For", "192.168.1.100")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

        htmlContent.subscribe(System.out::println, e -> System.err.println(e.getMessage()));
    }

    /**
     * 测试多种断言组合方式路由
     * @throws Exception
     */
    @Test
    public void testMultiPredicatesRoute() throws Exception {

        String responseBody = webTestClient.get().uri("http://localhost:8080/test?name=bonny&foo=bar")
                .header("X-Request-Id", "abc123")
                .header("Host", "test.com")
                .cookie("eid", "10086")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult().getResponseBody();
        System.out.println("responseBody = " + responseBody);

    }

}
