package com.zazhi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zazhi.common.enums.JudgeStatus;
import com.zazhi.common.pojo.entity.JudgeResult;
import com.zazhi.common.utils.MessageQueueUtil;
import com.zazhi.pojo.dto.JudgeDTO;
import com.zazhi.pojo.dto.SubmissionQueryDTO;
import com.zazhi.mapper.JudgeMapper;
import com.zazhi.mapper.ProblemMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.pojo.entity.*;
import com.zazhi.pojo.result.PageResult;
import com.zazhi.service.JudgeService;
import com.zazhi.common.utils.ThreadLocalUtil;
import com.zazhi.pojo.vo.SubmissionInfoVO;
import com.zazhi.pojo.vo.SubmissionPageVO;
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
        // 记录任务状态, pending、judging、finished ( 、判题中、判题完成)
        // (生成submission存入数据库)
        Long userId = ThreadLocalUtil.getCurrentId();

//        judgeDTO.setUserId(ThreadLocalUtil.getCurrentId());
        Submission submission = Submission.builder()
                .userId(userId)
                .problemId(judgeDTO.getProblemId())
                .contestId(0) // TODO: 比赛id
                .code(judgeDTO.getCode())
                .language(judgeDTO.getLanguage())
                .status(JudgeStatus.PENDING)
                .build();
        judgeMapper.insertSubmission(submission);

        // 生成提交id ( 将submission的主键作为任务id )
        Long taskId = submission.getId();

        // TODO 可以优化为一次查询
        Problem problem = problemMapper.getById(judgeDTO.getProblemId());
        List<TestCase> testCases = problemMapper.getTestCases(problem.getId());

        // 生成判题任务
        JudgeTask judgeTask = JudgeTask.builder()
                .taskId(taskId)
                .problemId(judgeDTO.getProblemId())
                .userId(userId)
                .language(judgeDTO.getLanguage())
                .code(judgeDTO.getCode())
                .timeLimit(problem.getTimeLimit())
                .memoryLimit(problem.getMemoryLimit())
                .submissionTime(submission.getSubmitTime())
                .testCases(testCases)
//                .judgeType("ACM")
                .fullJudge(true) // TODO 暂时写死为全量评测
                .retryCount(0)
                .build();
        messageQueueUtil.sendJudgeTask(judgeTask);
        return taskId;
    }

    /**
     * 分页条件查询提交记录列表
     * @param submissionQueryDTO
     * @return
     */
    public PageResult<SubmissionPageVO> getSubmissions(SubmissionQueryDTO submissionQueryDTO) {
        PageHelper.startPage(submissionQueryDTO.getCurrentPage(), submissionQueryDTO.getLimit());
        Page<SubmissionPageVO> res = judgeMapper.getSubmissions(submissionQueryDTO);
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
}
