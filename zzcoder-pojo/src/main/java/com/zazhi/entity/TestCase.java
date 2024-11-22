package com.zazhi.entity;

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
        private String expectedOutput;
        private Timestamp createTime;
        private Timestamp updateTime;
}