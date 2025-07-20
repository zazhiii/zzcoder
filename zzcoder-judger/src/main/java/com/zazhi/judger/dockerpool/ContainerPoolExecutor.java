package com.zazhi.judger.dockerpool;


import com.zazhi.judger.common.exception.SystemException;
import com.zazhi.judger.dockerpool.containers.DockerContainer;
import com.zazhi.judger.dockerpool.factorys.DockerContainerFactory;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
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

    @Getter
    private final Set<T> allContainers = ConcurrentHashMap.newKeySet();

//    private final AtomicInteger containerCount;

    public ContainerPoolExecutor(int maximumPoolSize,
                                 long keepStartTime,
                                 TimeUnit unit,
                                 DockerContainerFactory<T> dockerContainerFactory) {
        this.maximumPoolSize = maximumPoolSize;
        this.keepStartTime = unit.toMillis(keepStartTime);
        this.dockerContainerFactory = dockerContainerFactory;
//        this.containerCount = new AtomicInteger(0);

        // 启动定时任务停止空闲容器
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::cleanIdleContainers,
                1, 1, TimeUnit.SECONDS);
    }

    public T acquireContainer(){
        T container = containerQueue.poll();
        // 如果没有可用的容器，尝试创建新的容器
        if (container == null) {
            if (allContainers.size() < maximumPoolSize) {
                // 创建新的容器
                container = dockerContainerFactory.createDockerContainer();
//                containerCount.incrementAndGet();
                allContainers.add(container);
            } else {
                // 等待直到有可用的容器
                try {
                    container = containerQueue.take();
                } catch (InterruptedException e) {
                    throw new SystemException("没有获取到容器");
                }
            }
        }
        if(container == null){
            throw new SystemException("没有可用的容器");
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