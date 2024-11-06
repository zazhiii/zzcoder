package com.zazhi.mapper;

import com.zazhi.dto.ProblemDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProblemMapper {
    /**
     * 添加题目
     *
     * @param problemDTO
     */
    void insert(ProblemDTO problemDTO);
}
