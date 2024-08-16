package com.springcloud.gateway;

import jakarta.servlet.http.Cookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCookieRoute() throws Exception {

        // 执行模拟的HTTP请求
        mockMvc.perform(
                MockMvcRequestBuilders.get("/getAppName")
                        .cookie(new Cookie("sessionId", "test")) // 添加cookie
                )
                .andExpect(MockMvcResultMatchers.status().isOk()) // 断言响应状态为200
                .andDo(MockMvcResultHandlers.print()); // 直接打印结果
    }

}
