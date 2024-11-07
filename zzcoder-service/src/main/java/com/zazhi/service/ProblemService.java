package com.zazhi.service;

import com.zazhi.dto.ProblemDTO;
import com.zazhi.dto.ProblemQueryDTO;
import com.zazhi.entity.Problem;
import com.zazhi.result.PageResult;
import com.zazhi.result.Result;
import com.zazhi.vo.ProblemVO;

import java.util.List;

public interface ProblemService {
    /**
     * 添加题目
     *
     * @param problemDTO
     */
    void addProblem(ProblemDTO problemDTO);

    /**
     * 题目条件分页查询
     *
     * @param problemQueryDTO
     * @return
     */
    PageResult<ProblemVO> page(ProblemQueryDTO problemQueryDTO);

    /**
     * 更新题目信息
     *
     * @param problemDTO
     */
    void update(ProblemDTO problemDTO);

    /**
     * 删除题目
     *
     * @param id
     */
    void deleteProblemWithTags(Integer id);

    /**
     * 获取单个题目信息
     * @param id
     * @return
     */
    Problem getProblemInfo(Integer id);

    /**
     * 添加标签到题目
     * @param problemId
     * @param tagIds
     */
    void addTagToProblem(Integer problemId, List<Integer> tagIds);

    /**
     * 删除题目上的标签
     * @param problemId
     * @param tagId
     */
    void deleteTagFromProblem(Integer problemId, Integer tagId);
}
