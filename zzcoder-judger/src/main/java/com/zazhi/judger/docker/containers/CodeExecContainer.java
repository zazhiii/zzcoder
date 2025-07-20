package com.zazhi.judger.docker.containers;

import com.github.dockerjava.api.DockerClient;
import lombok.Getter;

/**
 * @author zazhi
 * @date 2025/7/1
 * @description: CodeExecContainer class represents a container for executing code.
 */

@Getter
public class CodeExecContainer extends DockerContainer{

    private final String hostWorkingDir;

    public CodeExecContainer(DockerClient dockerClient, String containerId, String containerName, String hostWorkingDir) {
        super(dockerClient, containerId, containerName, System.currentTimeMillis());
        this.hostWorkingDir = hostWorkingDir;
    }
}