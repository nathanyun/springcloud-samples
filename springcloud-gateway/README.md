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