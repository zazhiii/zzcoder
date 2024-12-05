package com.zazhi.service;

import com.zazhi.dto.ProblemSetDTO;

public interface ProblemSetService {
    /**
     * 添加题单
     * @param problemSetDTO
     */
    void addProblemSet(ProblemSetDTO problemSetDTO);
}
