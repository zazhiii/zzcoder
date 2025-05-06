package com.zazhi.service;

import com.zazhi.pojo.dto.ProblemSetDTO;
import com.zazhi.pojo.entity.ProblemSet;
import com.zazhi.pojo.result.PageResult;
import com.zazhi.pojo.vo.ProblemSetVO;

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

    /**
     * 添加题目到题单
     * @param problemSetId
     * @param problemId
     */
    void addProblemToProblemSet(Integer problemSetId, Integer problemId);

    /**
     * 从题单删除题目
     * @param problemSetId
     * @param problemId
     */
    void deleteProblemFromProblemSet(Integer problemSetId, Integer problemId);

    /**
     * 题单详细信息
     * @param id
     * @return
     */
    ProblemSetVO getProblemSet(Integer id);

    /**
     * 删除题单
     * @param id
     */
    void deleteProblemSet(Integer id);
}
