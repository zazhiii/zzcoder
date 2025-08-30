package com.zazhi.service;

import com.zazhi.common.pojo.entity.JudgeResult;
import com.zazhi.common.pojo.dto.JudgeDTO;
import com.zazhi.common.pojo.dto.SubmissionPageDTO;
import com.zazhi.common.pojo.entity.TestCaseResult;
import com.zazhi.common.pojo.result.PageResult;
import com.zazhi.common.pojo.vo.SubmissionInfoVO;
import com.zazhi.common.pojo.vo.SubmissionPageVO;
import com.zazhi.common.pojo.vo.UserProblemSubmissionVO;

import java.util.List;

public interface JudgeService {
    /**
     * 提交代码
     * @param judgeDTO
     * @return
     */
    Long submitCode(JudgeDTO judgeDTO);

    /**
     * 分页条件查询提交记录列表
     * @param submissionPageDTO
     * @return
     */
    PageResult<SubmissionPageVO> pageSubmissions(SubmissionPageDTO submissionPageDTO);

    /**
     * 更新提交记录
     * @param judgeResult
     */
    void updateSubmission(JudgeResult judgeResult);

    /**
     * 获取提交记录详情
     * @param submitId
     * @return
     */
    SubmissionInfoVO getSubmissionInfo(Long submitId);

    /**
     * 获取某题目用户的所有提交记录
     * @param problemId
     * @return
     */
    List<UserProblemSubmissionVO> getUserSubmissionsByProblemId(Integer problemId);

    /**
     * 添加测试用例结果
     * @param testCaseResult
     */
    void addTestCaseResult(TestCaseResult testCaseResult);

    /**
     * 查询题目所有测试用例
     * @param problemId
     * @return
     */
    List<Long> getTestCaseIdsByProblemId(Integer problemId);

    /**
     *  通过提交ID获取该提交的测试用例数量
     * @param taskId
     * @return
     */
    Integer getTestCaseCountBySubmissionId(Long taskId);
}
