package com.zazhi.service.impl;

import com.zazhi.dto.ContestDTO;
import com.zazhi.entity.Contest;
import com.zazhi.mapper.ContestMapper;
import com.zazhi.service.ContestService;
import com.zazhi.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author zazhi
 * @date 2025/2/10
 * @description: 管理员比赛相关接口
 */
@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    ContestMapper contestMapper;

    /**
     * 添加比赛
     * @param contestDTO 比赛信息
     */
    public void createContest(ContestDTO contestDTO) {
        Contest contest = new Contest();
        BeanUtils.copyProperties(contestDTO, contest);
        contest.setStatus(0); // 默认未开始
        Long userId = ThreadLocalUtil.getCurrentId();
        contest.setCreateUser(userId); // 创建人
        contestMapper.insert(contest);
    }

    /**
     * 获取比赛列表
     * @return 比赛列表
     */
    public List<Contest> getContestList() {
        return contestMapper.getContestList();
    }

    /**
     * 修改比赛
     * @param contestDTO 比赛信息
     */
    public void updateContest(ContestDTO contestDTO) {
        Contest contest = new Contest();
        BeanUtils.copyProperties(contestDTO, contest);
        contestMapper.updateContest(contestDTO);
    }
}
