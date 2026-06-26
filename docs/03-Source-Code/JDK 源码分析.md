# JDK 源码分析

## 阅读目标

围绕平台高并发、异步任务和集合使用场景，重点阅读 JDK 集合、并发包和线程池源码。

## 阅读路径

- `HashMap`
- `ConcurrentHashMap`
- `ThreadPoolExecutor`
- `CompletableFuture`
- `AbstractQueuedSynchronizer`
- `ReentrantLock`
- `Semaphore`
- `ArrayBlockingQueue`
- `LinkedBlockingQueue`

## 与平台相关的问题

- 提交高峰时线程池如何处理新任务。
- 判题任务队列满了之后如何降级。
- 排行榜缓存使用并发集合时有哪些边界。
- 锁竞争时线程如何进入等待队列。
- `CompletableFuture` 异常如何传播。

## 建议记录格式

| 源码点 | 核心机制 | 项目关联 |
| --- | --- | --- |
| `ThreadPoolExecutor#execute` | 任务提交策略 | 判题 Worker |
| `CompletableFuture#thenCombine` | 异步结果组合 | 页面数据聚合 |
| `ConcurrentHashMap#putVal` | 并发写入 | 本地缓存 |

## 待补充

- 源码关键注释。
- 线程池参数推导。
- 并发问题复盘。
