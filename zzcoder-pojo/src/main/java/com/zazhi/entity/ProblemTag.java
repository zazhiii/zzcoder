package com.zazhi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemTag {

    private Integer id;

    private String name;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
