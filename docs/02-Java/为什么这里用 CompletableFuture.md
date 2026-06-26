# 为什么这里用 CompletableFuture

## 使用场景

在 CTF 平台中，`CompletableFuture` 适合用于多个独立任务的异步编排，例如：

- 聚合用户、比赛、排行榜等多个查询结果。
- 并行加载比赛详情页需要的多个数据块。
- 异步触发非核心链路，如日志记录、通知发送。
- 对判题前置检查做并行处理。

## 选择原因

- 能表达任务依赖关系。
- 支持并行执行和结果聚合。
- 比手动创建线程更容易接入线程池治理。
- 异常处理链路更清晰。

## 典型模式

```java
CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(
    () -> userService.getUser(userId),
    bizExecutor
);

CompletableFuture<Contest> contestFuture = CompletableFuture.supplyAsync(
    () -> contestService.getContest(contestId),
    bizExecutor
);

CompletableFuture<PageView> pageFuture = userFuture.thenCombine(
    contestFuture,
    PageView::new
);
```

## 注意事项

- 不要默认使用 `ForkJoinPool.commonPool()` 承载业务任务。
- 阻塞 I/O 任务和 CPU 任务应使用不同线程池。
- 每条异步链路都要处理异常。
- 注意超时控制，避免请求线程无限等待。
- 不要让异步任务绕开事务边界导致数据不一致。

## 待补充

- 项目中具体使用位置。
- 线程池参数配置。
- 异常处理规范。
