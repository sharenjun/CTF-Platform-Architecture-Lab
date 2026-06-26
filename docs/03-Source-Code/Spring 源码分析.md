# Spring 源码分析

## 阅读目标

通过源码理解 Spring 在平台中的核心机制，包括容器启动、依赖注入、AOP、事务和 Web 请求处理。

## 阅读路径

- `SpringApplication.run`
- `ApplicationContext` 创建和刷新
- `BeanFactory` 与 `BeanDefinition`
- Bean 实例化和依赖注入
- BeanPostProcessor
- AOP 代理创建
- 事务拦截器
- Spring MVC 请求分发

## 与平台相关的问题

- 为什么接口加了事务注解后能自动提交或回滚。
- 为什么某些内部方法调用不会触发事务代理。
- 请求如何从 Controller 流转到 Service。
- Spring Security 过滤器链如何接入请求流程。
- Bean 初始化过慢时如何定位。

## 建议记录格式

| 源码点 | 作用 | 项目关联 |
| --- | --- | --- |
| `AbstractApplicationContext#refresh` | 刷新容器 | 服务启动 |
| `DefaultListableBeanFactory` | Bean 管理 | 依赖注入 |
| `DispatcherServlet#doDispatch` | 请求分发 | API 接口 |

## 待补充

- 源码调用链图。
- 关键断点位置。
- 项目内真实案例。
