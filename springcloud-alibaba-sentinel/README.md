# 快速开始
1. 下载[Sentinel控制台]('https://github.com/alibaba/Sentinel/releases')工程

2. 启动 sentinel-dashboard
    ```bash 
    java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
    ```
3. 启动 `SentinelApplication`

## 特别说明
> 1. 如果 Sentinel 控制台没有显示我的应用，或者没有监控展示，如何排查？
当 __首次访问对应的资源后__，等待一段时间 即可在控制台上看到对应的应用以及相应的监控信息。
> 注意::: 一定是首次访问资源后,才会再页面显示.!!!
> 2. 只需为应用添加 spring-cloud-starter-alibaba-sentinel依赖，所有的HTTP接口都能获得Sentinel保护, 可通过如下配置关闭对微服务的保护
```properties
     #关闭sentinel对controller的url的保护
     spring.cloud.sentinel.filter.enabled=false
```












