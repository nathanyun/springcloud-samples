# 快速开始
## 项目结构说明
```text
.
├── README.md
├── pom.xml
├── springcloud-gateway-eureka-server // 服务注册中心
├── springcloud-gateway-provider01 // 服务提供者01服务
├── springcloud-gateway-provider02 // 服务提供者02服务
└── springcloud-gateway-server // 统一网关服务

```

## 测试
1. 依次启动 `eureka-server`, `provider01`, `provider02`, `gateway-server` 四个服务.
2. 浏览器访问: http://localhost:8080/p2/hello?name=abc, 请求会路由到 `provider02`服务. `gateway-server`的日志如下:
    > Mapping [Exchange: GET http://localhost:8080/p2/hello?name=abc] to Route{id='gateway-provider02-service', uri=http://localhost:8082, order=0, predicate=Paths: [/p2/**], match trailing slash: true, gatewayFilters=[], metadata={}}


### 负载均衡测试
配置路由
```yaml
spring:
  cloud:
    # 统一网关
    gateway:
      discovery:
        locator:
          enabled: true # 开启从注册中心创建动态路由的功能, 利用服务名进行路由.
      routes:
        - id: gateway-provider01-app-route-rule
          uri: lb://gateway-provider01-app
          # 断言访问p1/**的请求都路由到 gateway-provider01-app 这个服务, 如果有多台, 就进行负载均衡方式访问
          predicates:
            - Path=/p1/**
```
1. 依次启动`eureka-server:8761`, `provider01:8081`,`gateway-server:8080` 
2. 再复制 `provider01`服务, 环境变量server.port修改为 `8083` 用来模拟集群环境
3. 浏览器访问: `http://localhost:8080/p1/hello?name=bonny`
4. 请求会交替转发到 `provider01:8081` 和 `provider01:8083` 两台服务
- 响应如下:
   ```text
   // 第一次请求
   Hello, bonny ! Power by appName: gateway-provider01-app, serverPort: 8081
   // 第二次请求
   Hello, bonny ! Power by appName: gateway-provider01-app, serverPort: 8083
   ```