package com.zazhi.service;

import com.zazhi.dto.ProblemDTO;

public interface ProblemService {
    /**
     * 添加题目
     *
     * @param problemDTO
     */
    void addProblem(ProblemDTO problemDTO);
}
