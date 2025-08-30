package com.zazhi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zazhi.common.enums.JudgeStatus;
import com.zazhi.common.pojo.entity.*;
import com.zazhi.common.pojo.vo.ProblemWithTestCaseVO;
import com.zazhi.common.pojo.vo.UserProblemSubmissionVO;
import com.zazhi.common.utils.MessageQueueUtil;
import com.zazhi.common.pojo.dto.JudgeDTO;
import com.zazhi.common.pojo.dto.SubmissionPageDTO;
import com.zazhi.mapper.JudgeMapper;
import com.zazhi.mapper.ProblemMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.common.pojo.result.PageResult;
import com.zazhi.service.JudgeService;
import com.zazhi.common.utils.ThreadLocalUtil;
import com.zazhi.common.pojo.vo.SubmissionInfoVO;
import com.zazhi.common.pojo.vo.SubmissionPageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zazhi
 * @date 2024/11/12
 * @description: 判题模块接口实现
 */
@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {

    private final JudgeMapper judgeMapper;

    private final ProblemMapper problemMapper;

    private final UserMapper userMapper;

    private final MessageQueueUtil messageQueueUtil;

    /**
     * 提交代码
     * @param judgeDTO
     * @return
     */
    public Long submitCode(JudgeDTO judgeDTO) {
        // 保存提交记录
        Integer userId = ThreadLocalUtil.getCurrentId();
        Submission submission = Submission.builder()
                .userId(userId)
                .problemId(judgeDTO.getProblemId())
                .code(judgeDTO.getCode())
                .language(judgeDTO.getLanguage())
                .status(JudgeStatus.PENDING)
                .build();
        judgeMapper.insertSubmission(submission);

        // 查出题目信息和测试用例
        ProblemWithTestCaseVO problemWithTestCase = problemMapper.getProblemWithTestCases(judgeDTO.getProblemId());

        // 生成判题任务
        JudgeTask judgeTask = JudgeTask.builder()
                .taskId(submission.getId())
                .language(judgeDTO.getLanguage())
                .code(judgeDTO.getCode())
                .timeLimit(problemWithTestCase.getTimeLimit())
                .memoryLimit(problemWithTestCase.getMemoryLimit())
                .testCases(problemWithTestCase.getTestCases())
                .fullJudge(judgeDTO.getFullJudge())
                .build();
        messageQueueUtil.sendJudgeTask(judgeTask);
        return submission.getId();
    }

    /**
     * 分页条件查询提交记录列表
     * @param submissionPageDTO
     * @return
     */
    public PageResult<SubmissionPageVO> pageSubmissions(SubmissionPageDTO submissionPageDTO) {
        PageHelper.startPage(submissionPageDTO.getPage(), submissionPageDTO.getPageSize());
        Page<SubmissionPageVO> res = judgeMapper.pageSubmissions(submissionPageDTO);
        return new PageResult<>(res.getTotal(), res.getResult());
    }

    /**
     * 更新提交记录
     * @param judgeResult
     */
    public void updateSubmission(JudgeResult judgeResult) {
        Submission submission = new Submission();
        submission.setId(judgeResult.getTaskId());
        BeanUtils.copyProperties(judgeResult, submission);
        judgeMapper.updateSubmission(submission);
    }

    /**
     * 获取提交记录详情
     * @param submitId
     * @return
     */
    public SubmissionInfoVO getSubmissionInfo(Long submitId) {

        return judgeMapper.getSubmissionInfoById(submitId);
    }

    /**
     * 获取某题目用户的所有提交记录
     * @param problemId
     * @return
     */
    @Override
    public List<UserProblemSubmissionVO> getUserSubmissionsByProblemId(Integer problemId) {
        Integer userId = ThreadLocalUtil.getCurrentId();
        return judgeMapper.getUserSubmissionsByProblemId(userId, problemId);
    }

    /**
     * 添加测试用例结果
     * @param testCaseResult
     */
    @Override
    public void addTestCaseResult(TestCaseResult testCaseResult) {
        judgeMapper.addTestCaseResult(testCaseResult);
    }

    /**
     * 查询题目所有测试用例
     * @param problemId
     * @return
     */
    @Override
    public List<Long> getTestCaseIdsByProblemId(Integer problemId) {
        return judgeMapper.getTestCaseIdsByProblemId(problemId);
    }

    /**
     * 根据提交id获取测试用例数量
     * @param taskId
     * @return
     */
    @Override
    public Integer getTestCaseCountBySubmissionId(Long taskId) {
        return judgeMapper.getTestCaseCountBySubmissionId(taskId);
    }
}
