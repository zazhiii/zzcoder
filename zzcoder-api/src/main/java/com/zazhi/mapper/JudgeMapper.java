package com.zazhi.mapper;

import com.github.pagehelper.Page;
import com.zazhi.pojo.dto.SubmissionQueryDTO;
import com.zazhi.pojo.entity.Submission;
import com.zazhi.pojo.vo.SubmissionInfoVO;
import com.zazhi.pojo.vo.SubmissionPageVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JudgeMapper {

    /**
     * 分页条件查询提交记录列表
     * @param submissionQueryDTO
     * @return
     */
    Page<SubmissionPageVO> getSubmissions(SubmissionQueryDTO submissionQueryDTO);

    /**
     * 插入提交记录
     * @param submission
     */
    void insertSubmission(Submission submission);

    /**
     * 更新提交记录
     */
    void updateSubmission(Submission submission);

    /**
     * 获取提交记录详情
     * @param submitId
     * @return
     */
//    @Select("select * from submission where id = #{submitId}")
    SubmissionInfoVO getSubmissionInfoById(Long submitId);

}
