# RateLimiter 限流框架设计文档

## 1. 系统概述

RateLimiter 是一个基于 Redis 的分布式限流框架，支持多种限流策略，可以灵活应用于不同的限流场景。框架采用策略模式设计，便于扩展和维护。

## 2. 核心功能

### 2.1 限流策略

框架支持以下六种限流策略：

1. 固定窗口（Fixed Window）
2. 滑动窗口（Sliding Window）
3. 令牌桶（Token Bucket）
4. 漏桶（Leaky Bucket）
5. 计数器（Counter）
6. 并发量控制（Concurrent）

### 2.2 限流维度

支持以下限流维度：

- 默认维度（DEFAULT）：基于接口级别的限流
- 用户维度（USER）：基于用户ID的限流
- IP维度（IP）：基于客户端IP的限流

## 3. 参数配置说明

### 3.1 通用参数

所有限流策略都支持以下基础参数：

- `timeWindow`：时间窗口大小（秒）
  - 固定窗口/滑动窗口：统计时间窗口
  - 令牌桶/漏桶：令牌产生或漏水的计算周期
  - 计数器：统计周期
  - 并发量：并发请求的超时时间

- `maxPermits`：最大许可数
  - 固定窗口/滑动窗口：窗口内最大请求数
  - 令牌桶：桶容量
  - 漏桶：桶容量
  - 计数器：周期内最大请求数
  - 并发量：最大并发请求数

### 3.2 特定策略参数

#### 3.2.1 令牌桶策略

- `tokenRate`：令牌生成速率（个/秒）
- `bucketCapacity`：令牌桶容量

#### 3.2.2 漏桶策略

- `outflowRate`：漏水速率（请求/秒）
- `bucketCapacity`：漏桶容量

## 4. 使用方法

### 4.1 创建限流器

```java
// 方式1：直接创建
RateLimitStrategy strategy = RateLimitStrategyFactory.createStrategy(
    "fixed_window",    // 策略类型
    1,                 // 时间窗口（秒）
    10,                // 最大许可数
    LimitType.DEFAULT  // 限流维度
);

// 方式2：通过配置创建
RateLimitConfig config = new RateLimitConfig();
config.setTimeWindow(1);
config.setMaxPermits(10);
config.setLimitType(LimitType.DEFAULT);
config.setStrategy("fixed_window");
RateLimitStrategy strategy = RateLimitStrategyFactory.createStrategy(config);
```

### 4.2 执行限流

```java
boolean allowed = strategy.execute("resourceKey");
if (allowed) {
    // 请求允许通过
} else {
    // 请求被限流
}
```

## 5. 执行效果

### 5.1 固定窗口

- 特点：在固定时间窗口内限制请求数量
- 优点：实现简单，内存占用小
- 缺点：窗口边界效应，可能出现突刺流量

### 5.2 滑动窗口

- 特点：将时间窗口分割为多个小区间，平滑限流效果
- 优点：限流更平滑，避免突刺流量
- 缺点：实现相对复杂，内存占用较大

### 5.3 令牌桶

- 特点：按固定速率生成令牌，支持突发流量
- 优点：可以处理突发流量，限流平滑
- 缺点：令牌生成和消费的计算复杂度较高

### 5.4 漏桶

- 特点：请求以固定速率处理，严格控制流量
- 优点：流量最平滑，适合需要严格速率控制的场景
- 缺点：不支持突发流量，可能造成较高的请求延迟

### 5.5 计数器

- 特点：在时间周期内进行计数限流
- 优点：实现简单，易于理解
- 缺点：可能出现临界问题

### 5.6 并发量控制

- 特点：控制同时进行的请求数量
- 优点：直接控制系统负载
- 缺点：无法精确控制请求速率

## 6. 扩展性设计

框架通过抽象类 `RateLimitStrategy` 定义了限流策略的基本接口，新增限流策略只需：

1. 继承 `RateLimitStrategy` 抽象类
2. 实现必要的抽象方法
3. 在 `RateLimitStrategyFactory` 中注册新策略

## 7. 注意事项

1. Redis 配置：确保 Redis 服务可用且性能足够
2. 参数配置：根据实际场景合理设置时间窗口和限制数量
3. 限流维度：选择合适的限流维度，避免限流粒度过粗或过细
4. 性能考虑：合理使用限流策略，避免过多的 Redis 操作影响性能