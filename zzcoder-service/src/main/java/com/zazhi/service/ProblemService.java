package com.zazhi.service;

import com.zazhi.dto.ProblemDTO;
import com.zazhi.dto.ProblemQueryDTO;
import com.zazhi.entity.Problem;
import com.zazhi.entity.TestCase;
import com.zazhi.result.PageResult;
import com.zazhi.result.Result;
import com.zazhi.vo.ProblemInfoVO;
import com.zazhi.vo.ProblemVO;

import java.util.List;

public interface ProblemService {
    /**
     * 添加题目
     *
     * @param problem
     */
    void addProblem(Problem problem);

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
     * @param problem
     */
    void update(Problem problem);

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
    ProblemInfoVO getProblemInfo(Integer id);

    /**
     * 添加标签到题目
     * @param problemId
     * @param tagId
     */
    void addTagToProblem(Integer problemId, Integer tagId);

    /**
     * 删除题目上的标签
     * @param problemId
     * @param tagId
     */
    void deleteTagFromProblem(Integer problemId, Integer tagId);

    /**
     * 为题目添加测试用例
     * @param testCase
     */
    void addTestCase(TestCase testCase);

    /**
     * 删除测试用例
     * @param id
     */
    void deleteTestCase(Integer id);

    /**
     * 获取题目的测试用例
     * @param problemId
     * @return
     */
    List<TestCase> getTestCases(Integer problemId);
}
