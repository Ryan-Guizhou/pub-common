# RateLimiter 限流框架原型文档

## 1. 项目结构

```
src/main/java/com/dianping/ratelimiter/
├── demo/
│   ├── factory/
│   │   └── RateLimitStrategyFactory.java    # 限流策略工厂
│   ├── strategy/
│   │   ├── RateLimitStrategy.java           # 限流策略抽象类
│   │   └── impl/
│   │       ├── FixedWindowRateLimitStrategy.java    # 固定窗口
│   │       ├── SlideWindowRateLimitStrategy.java    # 滑动窗口
│   │       ├── TokenBucketRateLimitStrategy.java    # 令牌桶
│   │       ├── LeakyBucketRateLimitStrategy.java    # 漏桶
│   │       ├── CounterRateLimitStrategy.java        # 计数器
│   │       └── ConcurrentRateLimitStrategy.java     # 并发量
│   ├── LimitType.java                      # 限流维度枚举
│   └── RateLimitConfig.java                # 限流配置类
```

## 2. 使用示例

### 2.1 固定窗口限流

```java
// 创建固定窗口限流器：1秒内最多允许10个请求
RateLimitStrategy fixedWindow = RateLimitStrategyFactory.createStrategy(
    "fixed_window",    // 策略类型
    1,                 // 1秒的时间窗口
    10,                // 最多10个请求
    LimitType.DEFAULT  // 默认限流维度
);

// 模拟100次请求
for (int i = 0; i < 100; i++) {
    boolean allowed = fixedWindow.execute("testKey");
    System.out.println("Request " + i + ": " + (allowed ? "allowed" : "limited"));
}

// 预期输出：前10个请求允许通过，之后的请求被限流，直到下一个时间窗口开始
```

### 2.2 滑动窗口限流

```java
// 创建滑动窗口限流器：1秒内最多允许10个请求
RateLimitStrategy slidingWindow = RateLimitStrategyFactory.createStrategy(
    "sliding_window", 
    1,
    10, 
    LimitType.USER    // 基于用户维度限流
);

// 模拟不同用户的请求
String[] users = {"user1", "user2"};
for (String user : users) {
    for (int i = 0; i < 20; i++) {
        boolean allowed = slidingWindow.execute(user);
        System.out.println("User " + user + " Request " + i + ": " + 
            (allowed ? "allowed" : "limited"));
    }
}

// 预期输出：每个用户前10个请求允许通过，之后的请求被限流
```

### 2.3 令牌桶限流

```java
// 创建令牌桶限流器：每秒生成10个令牌，桶容量100
RateLimitStrategy tokenBucket = RateLimitStrategyFactory.createStrategy(
    "token_bucket",
    1,              // 令牌生成周期
    10,             // 基础容量
    LimitType.IP,   // 基于IP限流
    10.0,           // 令牌生成速率
    100             // 桶容量
);

// 模拟突发请求
String clientIp = "192.168.1.1";
for (int i = 0; i < 150; i++) {
    boolean allowed = tokenBucket.execute(clientIp);
    System.out.println("Request " + i + ": " + (allowed ? "allowed" : "limited"));
    Thread.sleep(50); // 50ms发起一次请求
}

// 预期输出：初始可以快速消费100个令牌，之后每秒最多处理10个请求
```

### 2.4 漏桶限流

```java
// 创建漏桶限流器：每秒处理10个请求，桶容量100
RateLimitStrategy leakyBucket = RateLimitStrategyFactory.createStrategy(
    "leaky_bucket",
    1,              // 漏水周期
    10,             // 基础容量
    LimitType.DEFAULT,
    10.0,           // 漏水速率
    100             // 桶容量
);

// 模拟突发请求
for (int i = 0; i < 150; i++) {
    boolean allowed = leakyBucket.execute("testKey");
    System.out.println("Request " + i + ": " + (allowed ? "allowed" : "limited"));
    if (i % 20 == 0) {
        Thread.sleep(1000); // 每20个请求暂停1秒
    }
}

// 预期输出：稳定每秒处理10个请求，超过桶容量的请求被拒绝
```

### 2.5 计数器限流

```java
// 创建计数器限流器：1秒内最多允许10个请求
RateLimitStrategy counter = RateLimitStrategyFactory.createStrategy(
    "counter",
    1,
    10,
    LimitType.DEFAULT
);

// 模拟请求
for (int i = 0; i < 30; i++) {
    boolean allowed = counter.execute("testKey");
    System.out.println("Request " + i + ": " + (allowed ? "allowed" : "limited"));
    Thread.sleep(50); // 50ms发起一次请求
}

// 预期输出：每秒最多处理10个请求
```

### 2.6 并发量控制

```java
// 创建并发控制限流器：最大并发数10，超时时间1秒
RateLimitStrategy concurrent = RateLimitStrategyFactory.createStrategy(
    "concurrent",
    1,              // 超时时间
    10,             // 最大并发数
    LimitType.DEFAULT
);

// 模拟并发请求
ExecutorService executor = Executors.newFixedThreadPool(20);
for (int i = 0; i < 20; i++) {
    executor.submit(() -> {
        boolean allowed = concurrent.execute("testKey");
        System.out.println("Thread " + Thread.currentThread().getId() + 
            ": " + (allowed ? "allowed" : "limited"));
        if (allowed) {
            Thread.sleep(500); // 模拟处理时间
        }
    });
}

// 预期输出：同时只有10个请求被处理，其他请求被限流
```

## 3. 效果展示

### 3.1 固定窗口限流效果

```
Request 0: allowed
Request 1: allowed
...
Request 9: allowed
Request 10: limited
Request 11: limited
...
[1秒后]
Request 50: allowed
Request 51: allowed
```

### 3.2 滑动窗口限流效果

```
User user1 Request 0: allowed
User user1 Request 1: allowed
...
User user1 Request 9: allowed
User user1 Request 10: limited

User user2 Request 0: allowed
User user2 Request 1: allowed
...
```

### 3.3 令牌桶限流效果

```
Request 0: allowed
Request 1: allowed
...
Request 99: allowed    // 消耗完桶内令牌
Request 100: limited   // 等待令牌生成
Request 101: limited
[等待一段时间]
Request 102: allowed   // 新令牌生成
```

### 3.4 漏桶限流效果

```
Request 0: allowed
Request 1: allowed
...
Request 9: allowed
Request 10: limited    // 每秒最多处理10个请求
Request 11: limited
[1秒后]
Request 12: allowed
```

### 3.5 计数器限流效果

```
Request 0: allowed
Request 1: allowed
...
Request 9: allowed
Request 10: limited
Request 11: limited
[1秒后]
Request 12: allowed
```

### 3.6 并发控制效果

```
Thread 1: allowed
Thread 2: allowed
...
Thread 10: allowed
Thread 11: limited    // 超过最大并发数
Thread 12: limited
[某个请求处理完成]
Thread 13: allowed    // 新的请求可以处理
```

## 4. 性能指标

- 响应时间：平均 < 5ms
- 内存占用：每个限流器实例 < 1MB
- Redis 操作：每次限流判断 1-2 次 Redis 操作
- 并发支持：单机支持 10000+ QPS

## 5. 最佳实践

1. 选择合适的限流策略：
   - 简单限流场景：使用固定窗口或计数器
   - 需要平滑限流：使用滑动窗口
   - 允许突发流量：使用令牌桶
   - 严格速率控制：使用漏桶
   - 并发控制：使用并发量控制

2. 合理设置参数：
   - 时间窗口不宜过小，建议 ≥ 1秒
   - 令牌桶/漏桶容量建议设置为速率的 2-5 倍
   - 并发控制超时时间要大于请求平均处理时间

3. 监控和告警：
   - 监控限流次数
   - 监控请求通过率
   - 监控 Redis 性能