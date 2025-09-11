package com.zazhi.mapper;

import com.github.pagehelper.Page;
import com.zazhi.common.enums.ContestStatus;
import com.zazhi.common.pojo.dto.ContestDTO;
import com.zazhi.common.pojo.entity.Contest;
import com.zazhi.common.pojo.vo.ContestPageVO;
import com.zazhi.common.pojo.vo.ContestProblemVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContestMapper {

    /**
     * 添加比赛
     * @param contest 比赛信息
     */
    @Insert("insert into contest(title, description, start_time, end_time, duration, status, visible, type, password, create_user) " +
            "values(#{title}, #{description}, #{startTime}, #{endTime}, #{duration}, #{status}, #{visible}, #{type}, #{password}, #{createUser})")
    void insert(Contest contest);

    /**
     * 获取当前用户创建的比赛列表
     * @return 比赛列表
     */
    @Select("select * from contest where create_user = #{userId}")
    List<Contest> getContestList(Integer userId);

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
    @Update("update contest set status = #{status.code} where id = #{id}")
    void updateContestStatus(Long id, ContestStatus status);

    /**
     * 报名比赛
     * @param contestId
     */
    @Insert("insert into contest_user(contest_id, user_id) values(#{contestId}, #{userId})")
    void registerContest(Long contestId, Integer userId);

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

    /**
     * 获取比赛中的题目
     * @param contestId
     * @return
     */
    List<ContestProblemVO> getContestProblems(Long contestId);

    /**
     * 获取比赛中和未开始的比赛
     * @return
     */
    @Select("select * from contest where status = 0 or status = 1")
    List<Contest> getActiveOrUpcomingContests();

    /**
     * 分页查询比赛
     * @param keyword
     * @param status
     * @param type
     * @return
     */
    Page<ContestPageVO> page(String keyword, Integer status, Integer type);

    /**
     * 查询是否报名
     * @param contestId
     * @param userId
     * @return
     */
    @Select("select count(*) from contest_user " +
            "where contest_id = #{contestId} and user_id = #{userId}")
    Integer CountContestUser(Long contestId, Integer userId);
}
