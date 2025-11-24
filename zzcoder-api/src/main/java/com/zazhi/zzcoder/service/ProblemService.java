package com.zazhi.zzcoder.service;

import com.zazhi.zzcoder.common.pojo.dto.ProblemPageDTO;
import com.zazhi.zzcoder.common.pojo.entity.Problem;
import com.zazhi.zzcoder.common.pojo.entity.TestCase;
import com.zazhi.zzcoder.common.pojo.result.PageResult;
import com.zazhi.zzcoder.common.pojo.vo.ProblemInfoVO;
import com.zazhi.zzcoder.common.pojo.vo.ProblemPageVO;
import com.zazhi.zzcoder.common.pojo.vo.TagVO;

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
     * @param problemPageDTO
     * @return
     */
    PageResult<ProblemPageVO> page(ProblemPageDTO problemPageDTO);

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

    /**
     * 根据题目id查询题目的标签
     * @param problemId
     * @return
     */
    List<TagVO> getProblemTags(Integer problemId);

    /**
     * 搜索题目
     * @param keyword 关键词
     * @return 题目列表
     */
    List<ProblemPageVO> searchProblem(String keyword);
}
