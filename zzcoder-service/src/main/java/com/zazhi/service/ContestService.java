package com.zazhi.service;


import com.zazhi.dto.ContestDTO;
import com.zazhi.entity.Contest;

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
}
