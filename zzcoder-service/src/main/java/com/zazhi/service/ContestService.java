package com.zazhi.service;


import com.zazhi.dto.ContestDTO;
import com.zazhi.entity.Contest;
import com.zazhi.vo.ContestVO;

import java.util.List;

public interface ContestService {

    /**
     * 添加比赛
     * @param contestDTO 比赛信息
     */
    void createContest(ContestDTO contestDTO);


    /**
     * 获取我创建的比赛列表
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
    void updateContestStatus(Long id, int status);


    /**
     * 报名比赛
     * @param contestId
     */
    void registeContest(Long contestId);
}
