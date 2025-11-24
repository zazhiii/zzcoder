package com.zazhi.zzcoder.judger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/**
 * @author zazhi
 * @date 2024/11/17
 * @description: TODO
 */

@SpringBootTest
@Slf4j
public class sandBoxTest {

    @Autowired
    private DockerClient dockerClient;

    @Test
    public void test() {

        // 代码允许允许的最大时间15秒
        final long TIME_OUT = 5 * 1000L;

        String codePath = "C:/tmp/judgeTask_47";
        CreateContainerResponse container = dockerClient.createContainerCmd("java_judger")
                .withHostConfig(
                        HostConfig.newHostConfig()
                                .withBinds(new Bind(codePath, new Volume("/app"))) // Mount path
                )
                .withWorkingDir("/app") // Set working directory
                .exec();

        // Start container
        dockerClient.startContainerCmd(container.getId()).exec();

        // 创建执行命令
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient
                .execCreateCmd(container.getId())
                .withCmd(
                        "java", "Main"
                )
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .exec();


        final long[] maxMemory = {-2L};

        // 获取占用的内存
        StatsCmd statsCmd = dockerClient.statsCmd(container.getId());
        ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>()
        {

            @Override
            public void onNext(Statistics statistics)
            {
                System.out.println("内存占用：" + statistics.getMemoryStats().getUsage());
                maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
            }
            @Override
            public void close()
            {
            }
            @Override
            public void onStart(Closeable closeable)
            {
            }
            @Override
            public void onError(Throwable throwable)
            {
            }
            @Override
            public void onComplete()
            {
            }
        });
        statsCmd.exec(statisticsResultCallback);
        statsCmd.start();

        final String[] message = {null};
        final String[] errorMessage = {null};
        long time = 0L;
        // 判断是否超时
        // todo 利用超时标志
        final boolean[] timeout = {true};
        String execId = execCreateCmdResponse.getId();

        ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback()
        {
            @Override
            public void onComplete() {
                // 如果执行完成，则表示没超时
                timeout[0] = false;
                super.onComplete();
            }

            @Override
            public void onNext(Frame frame) {
                StreamType streamType = frame.getStreamType();
                System.out.println(new String(frame.getPayload()));
                System.out.println("内存占用-zzx：" + maxMemory[0]);
                if (StreamType.STDERR.equals(streamType)){
                    if (errorMessage[0] == null){
                        errorMessage[0] = new String(frame.getPayload());
                    }else{
                        errorMessage[0] += new String(frame.getPayload());
                    }
                    System.out.println("输出错误结果：" + errorMessage[0]);
                }else{
                    if (message[0] == null){
                        message[0] = new String(frame.getPayload());
                    }else{
                        message[0] += new String(frame.getPayload());
                    }
                    System.out.println("输出结果：" + message[0]);
                }
                super.onNext(frame);
            }
        };

        try{
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            dockerClient.execStartCmd(execId).exec(execStartResultCallback).awaitCompletion(TIME_OUT, TimeUnit.MICROSECONDS);
            stopWatch.stop();
            time = stopWatch.getLastTaskTimeMillis();
            statsCmd.close();
        }catch (InterruptedException e){
            log.error("程序执行异常");
            throw new RuntimeException(e);
        }
        while (maxMemory[0] == -2L || message[0] == null)
        {
            // 等待结果
        }

        System.out.println("输出结果：" + message[0]);
        System.out.println("错误结果：" + errorMessage[0]);
        System.out.println("内存占用：" + maxMemory[0]);
        System.out.println("执行时间：" + time);
    }
}
