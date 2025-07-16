package com.zazhi.judger.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zazhi
 * @date 2025/7/2
 * @description: CodeRunResult 类表示代码运行的结果
 */
@Data
@AllArgsConstructor
public class CodeRunResult {
    private String stdout;
    private String stderr;
    private Long timeUsed;
    private Long memoryUsed;
    private Boolean timeout;

    public static CodeRunResult success(String stdout, long timeUsed, long memoryUsed) {
        return new CodeRunResult(stdout, "", timeUsed, memoryUsed, false);
    }

    public static CodeRunResult error(String err){
        return new CodeRunResult("", err, 0L, 0L, false);
    }

    public static CodeRunResult timeout() {
        return new CodeRunResult("", "", 0L, 0L, true);
    }
}