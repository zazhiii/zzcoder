package com.zazhi.judger.docker;


import com.zazhi.judger.docker.containers.DockerContainer;
import com.zazhi.judger.docker.factorys.DockerContainerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zazhi
 * @date 2025/7/2
 * @description: ContainerPoolExecutor 类用于管理容器池的执行器
 */
public class ContainerPoolExecutor<T extends DockerContainer> {

    private final int maximumPoolSize;

    private final long keepStartTime;

    private final DockerContainerFactory<T> dockerContainerFactory;

    private final LinkedBlockingQueue<T> containerQueue = new LinkedBlockingQueue<>();

    private final AtomicInteger containerCount;

    public ContainerPoolExecutor(int maximumPoolSize,
                                 long keepStartTime,
                                 TimeUnit unit,
                                 DockerContainerFactory<T> dockerContainerFactory) {
        this.maximumPoolSize = maximumPoolSize;
        this.keepStartTime = unit.toMillis(keepStartTime);
        this.dockerContainerFactory = dockerContainerFactory;
        this.containerCount = new AtomicInteger(0);

        // 启动定时任务停止空闲容器
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::cleanIdleContainers,
                1, 1, TimeUnit.SECONDS);
    }

    public T acquireContainer() throws InterruptedException {
        T container = containerQueue.poll();
        if (container == null) {
            if (containerCount.get() < maximumPoolSize) {
                // 创建新的容器
                container = dockerContainerFactory.createDockerContainer();
                containerCount.incrementAndGet();
            } else {
                // 等待直到有可用的容器
                container = containerQueue.poll();
            }
        }
        if(container == null){
            throw new InterruptedException("No available container in the pool");
        }
        if(!container.isRunning()){
            container.start();
        }
        container.setLastUsedTime(System.currentTimeMillis());
        return container;
    }

    public void releaseContainer(T container) {
        if (container != null) {
            containerQueue.offer(container);
        }
    }

    private void cleanIdleContainers(){
        long currentTime = System.currentTimeMillis();
        containerQueue.forEach(container -> {
            if (currentTime - container.getLastUsedTime() > keepStartTime) {
                container.stop();
            }
        });
    }

}