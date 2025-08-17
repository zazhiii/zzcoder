package com.zazhi.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {
        private String id;
        private String problemId;
        private String input;
        private String output;
        private Integer isSample;
        private Timestamp createTime;
        private Timestamp updateTime;
}