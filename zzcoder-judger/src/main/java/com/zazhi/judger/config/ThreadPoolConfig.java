package com.zazhi.judger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService executorService() {
        // 核心线程数
        int corePoolSize = 16;
        // 最大线程数
        int maximumPoolSize = 32;
        // 线程最大空闲时间
        long keepAliveTime = 60L; // 60秒
        // 创建线程的工厂
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        // 任务队列
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(100);
        // 拒绝策略
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy(); // 任务被拒绝时，主线程直接执行任务

        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());  // 你可以根据需求选择不同的拒绝策略
    }
}
