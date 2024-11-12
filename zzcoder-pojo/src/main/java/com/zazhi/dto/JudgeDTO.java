package com.zazhi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeDTO {
    private Long userId;
    private Long submitId;
    private Integer problemId;
    private String code;
    private String language;
}