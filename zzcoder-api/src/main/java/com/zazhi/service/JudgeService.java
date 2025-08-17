package com.zazhi.service;

import com.zazhi.common.pojo.entity.JudgeResult;
import com.zazhi.pojo.dto.JudgeDTO;
import com.zazhi.pojo.dto.SubmissionQueryDTO;
import com.zazhi.pojo.result.PageResult;
import com.zazhi.pojo.vo.SubmissionInfoVO;
import com.zazhi.pojo.vo.SubmissionPageVO;

public interface JudgeService {
    /**
     * 提交代码
     * @param judgeDTO
     * @return
     */
    Long submitCode(JudgeDTO judgeDTO);

    /**
     * 分页条件查询提交记录列表
     * @param submissionQueryDTO
     * @return
     */
    PageResult<SubmissionPageVO> getSubmissions(SubmissionQueryDTO submissionQueryDTO);

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
}
