package com.zazhi.common.pojo.vo;

import lombok.Data;

/**
 *
 * @author lixh
 * @since 2025/8/31 1:35
 */
@Data
public class ProblemSetUpdateDTO {
    private String title;          // 题单标题
    private String description;    // 题单描述
    private Integer status;        // 状态（0私有，1公开）
}
