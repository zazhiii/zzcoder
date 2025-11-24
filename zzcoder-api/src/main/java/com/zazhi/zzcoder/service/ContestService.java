package com.zazhi.zzcoder.service;


import com.zazhi.zzcoder.common.enums.ContestStatus;
import com.zazhi.zzcoder.common.pojo.dto.ContestDTO;
import com.zazhi.zzcoder.common.pojo.entity.Contest;
import com.zazhi.zzcoder.common.pojo.result.PageResult;
import com.zazhi.zzcoder.common.pojo.vo.ContestPageVO;
import com.zazhi.zzcoder.common.pojo.vo.ContestProblemVO;
import com.zazhi.zzcoder.common.pojo.vo.ContestVO;
import com.zazhi.zzcoder.common.pojo.vo.UpcomingContestVO;

import java.util.List;

public interface ContestService {

    /**
     * 添加比赛
     * @param contestDTO 比赛信息
     */
    void createContest(ContestDTO contestDTO);


    /**
     * 获取当前用户创建的比赛列表
     * @return 比赛列表
     */
    List<Contest> getContestList();

    /**
     * 修改比赛
     * @param contestDTO 比赛信息
     */
    void updateContest(ContestDTO contestDTO);

    /**
     * 删除比赛
     * @param id 比赛id
     */
    void deleteContest(Long id);

    /**
     * 获取比赛详细信息
     * @param id 比赛id
     * @return 比赛详细信息
     */
    ContestVO getContestDetail(Long id);

    /**
     * 获取所有比赛
     * @return
     */
    List<Contest> getAllContests();

    /**
     * 更新比赛状态
     * @param id 比赛id
     * @param status 比赛状态
     */
    void updateContestStatus(Long id, ContestStatus status);


    /**
     * 报名比赛
     * @param contestId
     */
    void registerContest(Long contestId);

    /**
     * 添加题目到比赛
     * @param contestId 比赛id
     * @param problemId 题目id
     */
    void addProblemToContest(Long contestId, Integer problemId, String displayId);

    /**
     * 获取比赛题目
     * @param contestId 比赛id
     * @return 比赛题目
     */
    List<ContestProblemVO> getContestProblems(Long contestId);

    /**
     * 获取比赛中和未开始的比赛
     * @return
     */
    List<Contest> getActiveOrUpcomingContests();

    /**
     * 分页查询公开比赛
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @param contestStatus
     * @param type
     * @return
     */
    PageResult<ContestPageVO> page(Integer pageNum, Integer pageSize, String keyword, Integer contestStatus, Integer type);

    /**
     * 从clist获取即将开始的比赛
     * @return 即将开始的比赛列表
     */
    List<UpcomingContestVO> getUpcomingContestsFromClist(Boolean upcoming, String resourceRegex);
}
