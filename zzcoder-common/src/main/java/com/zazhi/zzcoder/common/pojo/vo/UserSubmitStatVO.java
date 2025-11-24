package com.zazhi.zzcoder.common.pojo.vo;

import lombok.Data;

/**
 *
 * @author lixh
 * @since 2025/8/31 17:33
 */
@Data
public class UserSubmitStatVO {
    private Integer totalSubmit; // 总提交数
    private Integer acSubmit; // 通过数
    private Integer acProblem; // 通过题目数
}
