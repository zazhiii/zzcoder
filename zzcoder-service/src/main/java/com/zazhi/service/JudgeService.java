package com.zazhi.service;

import com.zazhi.dto.JudgeDTO;
import com.zazhi.dto.SubmissionQueryDTO;
import com.zazhi.entity.Submission;
import com.zazhi.result.PageResult;

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
    PageResult<Submission> getSubmissions(SubmissionQueryDTO submissionQueryDTO);
}
