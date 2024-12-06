package com.zazhi.mapper;

import com.github.pagehelper.Page;
import com.zazhi.dto.ProblemSetDTO;
import com.zazhi.entity.ProblemSet;
import com.zazhi.vo.ProblemSetVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProblemSetMapper {
    /**
     * 添加题单
     * @param problemSet
     */
    @Insert("insert into problem_set(title, description, status, create_user, update_user) values(#{title}, #{description}, 0, #{createUser}, #{updateUser})")
    void addProblemSet(ProblemSet problemSet);

    /**
     * 修改题单信息
     * @param problemSet
     */
    void updateProblemSet(ProblemSet problemSet);

    /**
     * 分页查询公开题单
     * @param title
     * @return
     */
    Page<ProblemSet> listPublicProblemSet(String title);

    /**
     * 查询我的所有题单
     * @return
     */
    @Select("select * from problem_set where create_user = #{currentId}")
    List<ProblemSet> listPrivateProblemSet(Long currentId);

    /**
     * 添加题目到题单
     * @param problemSetId
     * @param problemId
     */
    @Insert("insert into problem_problem_set(problem_set_id, problem_id) values(#{problemSetId}, #{problemId})")
    void addProblemToProblemSet(Integer problemSetId, Integer problemId);

    /**
     * 从题单删除题目
     * @param problemSetId
     * @param problemId
     */
    @Delete("delete from problem_problem_set where problem_set_id = #{problemSetId} and problem_id = #{problemId}")
    void deleteProblemFromProblemSet(Integer problemSetId, Integer problemId);

    /**
     * 题单详细信息
     * @param id
     * @return
     */
    ProblemSetVO getProblemSet(Integer id);
}
