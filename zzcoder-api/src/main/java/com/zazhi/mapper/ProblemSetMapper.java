package com.zazhi.mapper;

import com.github.pagehelper.Page;
import com.zazhi.common.pojo.entity.ProblemSet;
import com.zazhi.common.pojo.vo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProblemSetMapper {
    /**
     * 添加题单
     * @param problemSet
     */
    @Insert("insert into problem_set(title, description, status, create_user, update_user) " +
            "values(#{title}, #{description}, #{status}, #{createUser}, #{updateUser})")
    void addProblemSet(ProblemSet problemSet);

    /**
     * 修改题单信息
     * @param problemSet
     */
    void update(ProblemSet problemSet);

    /**
     * 分页查询公开题单
     * @param title
     * @return
     */
    Page<ProblemSetPageVO> pageProblemSet(String title, Integer status);

    /**
     * 查询我的所有题单
     * @return
     */
    @Select("select * from problem_set where create_user = #{currentId}")
    List<ProblemSetPageVO> listPrivateProblemSet(Integer currentId);

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
//    ProblemSetVO getProblemSetDetail(Integer id);


    /**
     * 查询题单题目数量
     * @param problemSetId 题单ID
     */
    @Select("select count(*) from problem_set_item where problem_set_id = #{problemSetId}")
    Integer getProblemCount(Integer problemSetId);

    /**
     * 删除题单
     * @param problemSetId
     */
    @Delete("delete from problem_set where id = #{problemSetId}")
    void deleteProblemSet(Integer problemSetId);

    @Select("select " +
            "ps.id, ps.title, ps.status, ps.description, ps.create_time, u.username as create_username, u.id as create_user_id " +
            "from problem_set ps, user u where ps.id = #{id} and ps.create_user = u.id")
    ProblemSetInfoVO getProblemSetInfo(Integer id);

    /**
     * 查询题单中本站题目
     * @param id 题单id
     * @return 题目列表
     */
    @Select("select p.id, p.problem_number, p.title, p.difficulty " +
            "from problem_set ps " +
            "         left join problem_set_item psi on ps.id = psi.problem_set_id " +
            "         inner join problem p on psi.problem_id = p.id " +
            "where ps.id = #{id}")
    List<ProblemSetItemInternalVO> getInternalProblems(Integer id);

    /**
     * 查询题单中外部题目
     * @param id 题单id
     * @return 题目列表
     */
    @Select("select p.id, p.title, p.difficulty, p.source, p.url " +
            "from problem_set ps " +
            "         left join problem_set_item psi on ps.id = psi.problem_set_id " +
            "         inner join external_problem p on psi.external_problem_id = p.id " +
            "where ps.id = #{id}")
    List<ProblemSetItemExternalVO> getExternalProblems(Integer id);

    @Select("select * from problem_set where id = #{id}")
    ProblemSet getById(Integer id);
}
