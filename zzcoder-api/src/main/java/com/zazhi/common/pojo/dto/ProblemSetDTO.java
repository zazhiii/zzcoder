package com.zazhi.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zazhi
 * @date 2024/12/5
 * @description: 题单DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSetDTO {
//    private Integer id;            // 主键，自增长
    private String title;          // 题单标题
    private String description;    // 题单描述
//    private Integer status;        // 状态（0私有，1公开）
//    private Long updateUser;       // 修改人（用户ID）
}
