package com.zazhi.dto;

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
    private String title;          // 题单标题
    private String description;    // 题单描述
}
