package com.zazhi.judger.dockerpool.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @author lixh
 * @since 2025/7/19 11:16
 */
@Data
@Builder
public class CmdExecResult {
    private String stdout;
    private String stderr;
    private boolean isTimeout;
    private int exitCode;
}
