# Spring Cloud 组件代码示例

## 开始

如无特别说明，项目代码均参考以下版本：

> jdk version : 17 +
>
> spring cloud version : 2022.0.5
>
> spring boot version : 3.0.13
>
> spring version : 6.0.14

## 代码说明

```text
.
├── README.md
├── springcloud-circuit-breaker // 断路器
├── springcloud-eureka // 服务注册发现
├── springcloud-gateway // 统一网关
├── springcloud-config // 配置中心
└── springcloud-openfeign // 声明式的Web服务客户端
```

## 特别说明

* springcloud 建议使用 Resilience4j 替换掉 Netfix Hystrix 来作为新的断路器实现，所以本项目不会提供hystrix的代码示例。

  * [GitHub说明]([https://github.com/spring-attic/hystrix-dashboard) `hystrix-dashboard is no longer actively maintained by VMware, Inc.`
  
  * > 从 spring cloud 2020.0.0（对应 spring boot 2.4.x） 开始，netflix 的几大组件，hystrix（熔断）、ribbon（负载均衡）、zuul（网关）、eureka（注册中心），除了 eureka 外，其他全部不能用了。能确保没问题的最新版本是 spring cloud Hoxton.SR12（对应 spring boot 2.3.12.RELEASE
  
* `springcloud-circuit-breaker` 模块 因为使用到了resilience4j 2.x版本，因此JDK版本必须位17以上。

## 链接

[Spring Cloud Resilience4j 文档](https://spring.io/projects/spring-cloud-circuitbreaker)

[Resilience4j中文文档](https://github.com/lmhmhl/Resilience4j-Guides-Chinese/blob/main/getting-start/Introduction.md)
