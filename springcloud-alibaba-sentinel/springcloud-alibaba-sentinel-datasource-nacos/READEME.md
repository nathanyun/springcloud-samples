# Sentinel规则持久化到Nacos 

## 快速开始
1. 安装并启动 nacos
2. 安装并启动 sentinel 控制台
3. 将 `src/main/resources/` 目录下的 sentinel 配置导入到 nacos 中
   - [sentinel-datasource-application-degrade-rules.json](src%2Fmain%2Fresources%2Fsentinel-datasource-application-degrade-rules.json)
   - [sentinel-datasource-application-flow-rules.json](src%2Fmain%2Fresources%2Fsentinel-datasource-application-flow-rules.json)
4. 分别启动 `SentinelDataSourceApplication` 和 `SentinelServiceProviderApplication`
5. 浏览器访问 `http://localhost:9802/hello` 测试慢调用降级
6. 访问 `http://localhost:9802/echo/abc` 测试限流