package com.zazhi.judger.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionStats {
    private Integer executionTime; // 执行时间 (毫秒)
    private Integer memoryUsage;   // 内存使用 (字节)
}
