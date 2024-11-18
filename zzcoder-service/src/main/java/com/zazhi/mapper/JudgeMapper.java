package com.zazhi.mapper;

import com.github.pagehelper.Page;
import com.zazhi.dto.SubmissionQueryDTO;
import com.zazhi.entity.Submission;
import com.zazhi.result.PageResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JudgeMapper {

    /**
     * 分页条件查询提交记录列表
     * @param submissionQueryDTO
     * @return
     */
    Page<Submission> getSubmissions(SubmissionQueryDTO submissionQueryDTO);

    /**
     * 插入提交记录
     * @param submission
     */
    void insertSubmission(Submission submission);

    /**
     * 更新提交记录
     */
    void updateSubmission(Submission submission);
}
