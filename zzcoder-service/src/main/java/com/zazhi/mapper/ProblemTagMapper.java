package com.zazhi.mapper;

import com.zazhi.entity.ProblemTag;
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
    @Insert("insert into problem_tag (pid, tid) values (#{problemId}, #{tagId})")
    void addTagToProblem(Integer tagId, Integer problemId);

    /**
     * 删除题目上的标签
     * @param problemId
     * @param tagId
     */
    @Delete("delete from problem_tag where pid = #{problemId} and tid = #{tagId}")
    void deleteTagFromProblem(Integer problemId, Integer tagId);
}
