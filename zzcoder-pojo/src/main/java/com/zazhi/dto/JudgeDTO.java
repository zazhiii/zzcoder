package com.zazhi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeDTO implements Serializable {
//    private Long userId;
    private Integer problemId;
    private String code;
    private String language;
}