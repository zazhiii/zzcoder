package com.zazhi.judger.service.impl;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.zazhi.common.pojo.vo.DockerContainerInfoVO;
import com.zazhi.judger.common.pojo.ContainerInfo;
import com.zazhi.judger.dockerpool.ContainerPoolExecutor;
import com.zazhi.judger.dockerpool.containers.CodeExecContainer;
import com.zazhi.judger.service.DockerPoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zazhi
 * @since 2025/7/20 17:39
 */
@Service
@RequiredArgsConstructor
public class DockerPoolServiceImpl implements DockerPoolService {
    private final ContainerPoolExecutor<CodeExecContainer> containerPoolExecutor;

    @Override
    public List<ContainerInfo> listAllContainers() {

        Set<CodeExecContainer> containers = containerPoolExecutor.getAllContainers();

        List<ContainerInfo> containerInfoList = new ArrayList<>();

        for(CodeExecContainer container : containers){
            InspectContainerResponse inspectContainerResponse = container.inspectContainer();

            // 从 Docker 容器检���响应中提取信息
            ContainerInfo containerInfo = new ContainerInfo();
            containerInfo.setId(inspectContainerResponse.getId());
            containerInfo.setName(inspectContainerResponse.getName());
            containerInfo.setImage(inspectContainerResponse.getConfig().getImage());

            // 设置容器状态
            String dockerState = inspectContainerResponse.getState().getStatus();
            containerInfo.setState(dockerState);

            // 根据 Docker 状态设置我们的业务状态
            if ("running".equals(dockerState)) {
                containerInfo.setStatus("running");
            } else if ("exited".equals(dockerState)) {
                containerInfo.setStatus("idle");
            } else {
                containerInfo.setStatus("error");
            }

            // 设置创建时间
            containerInfo.setCreatedTime(inspectContainerResponse.getCreated());

            // 设置内存限制和使用情况（如果有的话）
            if (inspectContainerResponse.getHostConfig() != null &&
                inspectContainerResponse.getHostConfig().getMemory() != null) {
                containerInfo.setMemoryLimit(inspectContainerResponse.getHostConfig().getMemory());
            }

            // 设置端口映射信息
            if (inspectContainerResponse.getNetworkSettings() != null &&
                inspectContainerResponse.getNetworkSettings().getPorts() != null) {
                containerInfo.setPorts(inspectContainerResponse.getNetworkSettings().getPorts().toString());
            }

            // 可以从容器对象中获取使用者信息（如果 CodeExecContainer 有这个信息的话）
            // containerInfo.setUsedBy(container.getUsedBy()); // 需要根据实际的 CodeExecContainer 实现

            containerInfoList.add(containerInfo);
        }

        return containerInfoList;
    }

    /**
     * 获取所有 Docker 容器的详细信息
     * @return
     */
    @Override
    public List<DockerContainerInfoVO> list() {

        return null; // TODO
    }
}
