package com.zazhi.service;


import com.zazhi.dto.ContestDTO;

public interface ContestService {

    /**
     * 添加比赛
     * @param contestDTO 比赛信息
     */
    void createContest(ContestDTO contestDTO);
}
