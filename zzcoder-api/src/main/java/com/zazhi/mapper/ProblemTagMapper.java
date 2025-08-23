package com.zazhi.mapper;

import com.zazhi.common.pojo.entity.ProblemTag;
import com.zazhi.common.pojo.entity.TestCase;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProblemTagMapper {

    /**
     * 根据题目id删除与标签的关联
     * @param pid
     */
    @Delete("delete from problem_tag where pid = #{pid}")
    void deleteTagByProblemId(Integer pid);

    /**
     * 根据题目id查询题目的标签
     * @param pid
     * @return
     */
    @Select("select t.* from tag t left outer join problem_tag p_t on p_t.tid = t.id where p_t.pid = #{pid}")
    List<ProblemTag> getTagByProblemId(Integer pid);

    /**
     * 添加标签到题目
     * @param tagId
     * @param problemId
     */
    @Insert("insert into problem_tag (problem_id, tag_id) values (#{problemId}, #{tagId})")
    void addTagToProblem(Integer tagId, Integer problemId);

    /**
     * 删除题目上的标签
     * @param problemId
     * @param tagId
     */
    @Delete("delete from problem_tag where problem_id = #{problemId} and tag_id = #{tagId}")
    void deleteTagFromProblem(Integer problemId, Integer tagId);

    /**
     * 为题目添加测试用例
     * @param testCase
     */
    @Insert("insert into test_case (problem_id, input, output, is_sample) values (#{problemId}, #{input}, #{output}, #{isSample})")
    void addTestCase(TestCase testCase);

    /**
     * 删除测试用例
     * @param id
     */
    @Delete("delete from test_case where id = #{id}")
    void deleteTestCase(Integer id);

    /**
     * 获取题目的测试用例
     * @param problemId
     * @return
     */
    @Select("select * from test_case where problem_id = #{problemId}")
    List<TestCase> getTestCases(Integer problemId);
}
