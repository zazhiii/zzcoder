package com.zazhi.service;

import com.zazhi.dto.ProblemSetDTO;
import com.zazhi.entity.ProblemSet;
import com.zazhi.result.PageResult;

import java.util.List;

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

    /**
     * 分页查询公开题单
     * @param page
     * @param size
     * @param title
     * @return
     */
    PageResult<ProblemSet> listPublicProblemSet(Integer page, Integer size, String title);


    /**
     * 查询我的所有题单
     * @return
     */
    List<ProblemSet> listPrivateProblemSet();
}
