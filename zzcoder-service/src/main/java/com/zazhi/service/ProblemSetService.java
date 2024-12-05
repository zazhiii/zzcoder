package com.zazhi.service;

import com.zazhi.dto.ProblemSetDTO;

public interface ProblemSetService {
    /**
     * 添加题单
     * @param problemSetDTO
     */
    void addProblemSet(ProblemSetDTO problemSetDTO);

    /**
     * 修改题单信息
     * @param problemSetDTO
     */
    void updateProblemSet(ProblemSetDTO problemSetDTO);
}
