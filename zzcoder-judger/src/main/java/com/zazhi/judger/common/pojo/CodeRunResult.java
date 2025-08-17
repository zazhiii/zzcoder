package com.zazhi.judger.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zazhi
 * @date 2025/7/2
 * @description: CodeRunResult 类表示代码运行的结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeRunResult {
    private String stdout;
    private String errMsg;
    private Long timeUsed = 0L;
    private Long memoryUsed = 0L;

    public CodeRunResult re(String errMsg) {
        return new CodeRunResult("", errMsg, 0L, 0L);
    }
}