package com.zazhi.judger.service;

import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import com.zazhi.judger.docker.ContainerPoolExecutor;
import com.zazhi.judger.docker.DockerContainerPool;
import com.zazhi.judger.docker.containers.CodeExecContainer;
import com.zazhi.judger.docker.containers.DockerContainer;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author lixinhuan
 * @date 2025/7/14
 * @description: JavaSandBox 类实现了 SandBox 接口，用于处理 Java 代码的判题任务
 */
@Service
@RequiredArgsConstructor
public class JavaSandBox implements SandBox{

    private final JavaExecutor javaExecutor;

    private final ContainerPoolExecutor<CodeExecContainer> pool;

    @Override
    public JudgeResult judge(JudgeTask task) {
        CodeExecContainer container = null;
        try{
            // 1. 获取容器
            container = pool.acquireContainer();

            // 2. 保存代码到容器的工作目录
            saveCode(task.getCode(),
                    container.getHostWorkingDir(),
                    javaExecutor.buildCodeFileName());

            // 3. 编译代码
            javaExecutor.compile(container);

            // 4. 运行代码
//            javaExecutor.execute(container,
//
//                    );


        }catch (Exception e){
            throw new RuntimeException("执行判题任务失败", e);
        }finally {
            pool.releaseContainer(container);
        }

        return null;
    }


    @Override
    public void saveCode(String code, String workDir, String fileName) {
        File dir = new File(workDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(code);
        } catch (IOException e) {
            throw new RuntimeException("保存代码文件失败", e);
        }
    }
}
