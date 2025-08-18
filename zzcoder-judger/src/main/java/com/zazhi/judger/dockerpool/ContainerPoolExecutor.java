package com.zazhi.judger.dockerpool;


import com.zazhi.judger.common.enums.ContainerStatus;
import com.zazhi.judger.common.exception.SystemException;
import com.zazhi.judger.dockerpool.containers.DockerContainer;
import com.zazhi.judger.dockerpool.factorys.DockerContainerFactory;
import lombok.Getter;

import java.util.List;
import java.util.Map;
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

    private final Map<String, ContainerStatus> containerStatusMap = new ConcurrentHashMap<>();

    public ContainerPoolExecutor(int maximumPoolSize,
                                 long keepStartTime,
                                 TimeUnit unit,
                                 DockerContainerFactory<T> dockerContainerFactory) {
        this.maximumPoolSize = maximumPoolSize;
        this.keepStartTime = unit.toMillis(keepStartTime);
        this.dockerContainerFactory = dockerContainerFactory;

        // 启动定时任务停止空闲容器
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::cleanIdleContainers,
                1, 1, TimeUnit.SECONDS);
    }

    public T acquireContainer(){
        T container;
        if(containerQueue.isEmpty() && getContainerCount() < maximumPoolSize){
            container = dockerContainerFactory.createDockerContainer();
        }else{
            try {
                container = containerQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException("获取容器时被中断", e);
            }
        }
        if(!container.isRunning()){
            container.start();
        }
        // 更新容器状态
        containerStatusMap.put(container.getContainerId(), ContainerStatus.BUSY);
        return container;
    }

    /**
     * 释放容器，将其放回池中
     * @param container 要释放的容器
     */
    public void releaseContainer(T container) {
        containerQueue.offer(container);
        containerStatusMap.put(container.getContainerId(), ContainerStatus.IDLE);
        container.setLastUsedTime(System.currentTimeMillis());
    }

    /**
     * 清理空闲容器，停止那些空闲时间超过 keepStartTime 的容器
     */
    private void cleanIdleContainers(){
        long currentTime = System.currentTimeMillis();
        containerQueue.forEach(container -> {
            if (currentTime - container.getLastUsedTime() > keepStartTime) {
                container.stop();
                containerStatusMap.put(container.getContainerId(), ContainerStatus.STOPPED);
            }
        });
    }

    /**
     * 获取当前容器池中的容器总数
     * @return 容器数量
     */
    public Integer getContainerCount(){
        return containerStatusMap.size();
    }

}