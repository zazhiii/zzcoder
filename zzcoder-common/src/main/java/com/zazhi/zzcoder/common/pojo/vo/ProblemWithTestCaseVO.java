package com.zazhi.zzcoder.common.pojo.vo;

import com.zazhi.zzcoder.common.pojo.entity.TestCase;
import lombok.Data;

import java.util.List;

/**
 *
 * @author lixh
 * @since 2025/8/25 0:21
 */
@Data
public class ProblemWithTestCaseVO {
    private Integer problemId;
    private Integer timeLimit;
    private Integer memoryLimit;
    private List<TestCase> testCases;
}
