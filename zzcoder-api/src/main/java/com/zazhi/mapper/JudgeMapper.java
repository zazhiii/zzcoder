package com.zazhi.mapper;

import com.github.pagehelper.Page;
import com.zazhi.common.pojo.entity.Submission;
import com.zazhi.common.pojo.entity.TestCaseResult;
import com.zazhi.common.pojo.vo.SubmissionInfoVO;
import com.zazhi.common.pojo.vo.SubmissionPageVO;
import com.zazhi.common.pojo.vo.UserProblemSubmissionVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JudgeMapper {

    /**
     * 分页条件查询提交记录列表
     * @param problemId
     * @param userId
     * @param username
     * @param status
     * @param language
     * @return
     */
    Page<SubmissionPageVO> pageSubmissions(Integer problemId, Integer userId, String username, String status, String language);

    /**
     * 插入提交记录
     * @param submission
     */
    @Insert("insert into submission (id, user_id, problem_id, contest_id, language, code, status, time_used, memory_used) " +
            "values (#{id}, #{userId}, #{problemId}, #{contestId}, #{language}, #{code}, #{status}, #{timeUsed}, #{memoryUsed})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
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

    @Select("select id, status, language, time_used, memory_used, create_time AS submitTime, error_message " +
            "from submission " +
            "where user_id = #{userId} and problem_id = #{problemId} " +
            "order by create_time desc")
    List<UserProblemSubmissionVO> getUserSubmissionsByProblemId(Integer userId, Integer problemId);


    @Insert("insert into test_case_result (test_case_id, submission_id, time_used, memory_used, status, error_message) " +
            "values (#{testCaseId}, #{submissionId}, #{timeUsed}, #{memoryUsed}, #{status}, #{errorMessage})")
    void addTestCaseResult(TestCaseResult testCaseResult);

    @Select("select id from test_case where problem_id = #{problemId}")
    List<Long> getTestCaseIdsByProblemId(Integer problemId);

    @Select("select count(*) from submission s left join test_case tc on s.problem_id = tc.problem_id where s.id = #{taskId}")
    Integer getTestCaseCountBySubmissionId(Long taskId);
}
