package com.zazhi.mapper;

import com.zazhi.dto.ContestDTO;
import com.zazhi.entity.Contest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContestMapper {

    /**
     * 添加比赛
     * @param contest 比赛信息
     */
    @Insert("insert into contest(title, description, start_time, end_time, status, visible, type, password, create_user) values(#{title}, #{description}, #{startTime}, #{endTime}, #{status}, #{visible}, #{type}, #{password}, #{createUser})")
    void insert(Contest contest);

    /**
     * 获取比赛列表
     * @return 比赛列表
     */
    @Select("select * from contest where create_user = #{userId}")
    List<Contest> getContestList(Long userId);

    /**
     * 修改比赛
     * @param contestDTO 比赛信息
     */
    void updateContest(ContestDTO contestDTO);

    /**
     * 删除比赛
     * @param id 比赛id
     */
    @Delete("delete from contest where id = #{id}")
    void deleteContest(Long id);

    /**
     * 获取比赛详细信息
     * @param id 比赛id
     * @return 比赛详细信息
     */
    @Select("select * from contest where id = #{id}")
    Contest getContestById(Long id);

    /**
     * 获取所有比赛
     * @return
     */
    @Select("select * from contest")
    List<Contest> getAllContests();

    /**
     * 更新比赛状态
     * @param id 比赛id
     * @param status 比赛状态
     */
    @Update("update contest set status = #{status} where id = #{id}")
    void updateContestStatus(Long id, int status);

    /**
     * 报名比赛
     * @param contestId
     */
    @Insert("insert into contest_user(contest_id, user_id) values(#{contestId}, #{userId})")
    void registeContest(Long contestId, Long userId);

    /**
     * 获取报名人数
     * @param contestId 比赛id
     */
    @Select("select count(*) from contest_user where contest_id = #{contestId}")
    int getRegisterCount(Long contestId);

    /**
     * 添加题目到比赛
     * @param contestId 比赛id
     * @param problemId 题目id
     * @param displayId 题目显示id
     */
    @Insert("insert into contest_problem(contest_id, problem_id, display_id) values(#{contestId}, #{problemId}, #{displayId})")
    void addProblemToContest(Long contestId, Integer problemId, String displayId);
}
