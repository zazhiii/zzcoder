package com.zazhi.service.impl;

import com.zazhi.dto.ContestDTO;
import com.zazhi.entity.Contest;
import com.zazhi.entity.Problem;
import com.zazhi.entity.User;
import com.zazhi.mapper.ContestMapper;
import com.zazhi.mapper.ProblemMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.service.ContestService;
import com.zazhi.utils.ThreadLocalUtil;
import com.zazhi.vo.ContestProblemVO;
import com.zazhi.vo.ContestVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

    @Autowired
    UserMapper userMapper;

    @Autowired
    ProblemMapper problemMapper;

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
        Long userId = ThreadLocalUtil.getCurrentId();
        return contestMapper.getContestList(userId);
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

    /**
     * 删除比赛
     * @param id 比赛id
     */
    public void deleteContest(Long id) {
        contestMapper.deleteContest(id);
    }

    /**
     * 获取比赛详细信息
     * @param ContestId 比赛id
     * @return 比赛详细信息
     */
    public ContestVO getContestDetail(Long ContestId) {
        Contest contest = contestMapper.getContestById(ContestId);
        if (contest == null) {
            throw new RuntimeException("比赛不存在"); // TODO: 抽取到常量类
        }
        ContestVO contestVO = new ContestVO();
        BeanUtils.copyProperties(contest, contestVO);
        // 设置创建人名称
        User user = userMapper.findById(contest.getCreateUser());
        contestVO.setCreateUserName(user.getUsername());
        // 设置比赛时长
        Duration duration = Duration.between(contest.getStartTime(), contest.getEndTime());
        contestVO.setDuration((int)duration.toMinutes());
        // 设置报名人数
        int count = contestMapper.getRegisterCount(ContestId);
        contestVO.setRegisterCount(count);
        return contestVO;
    }

    /**
     * 获取所有比赛
     * @return
     */
    public List<Contest> getAllContests() {
        return contestMapper.getAllContests();
    }

    /**
     * 更新比赛状态
     * @param id 比赛id
     * @param status 比赛状态
     */
    public void updateContestStatus(Long id, int status) {
        contestMapper.updateContestStatus(id, status);
    }

    /**
     * 报名比赛
     * @param contestId
     */
    public void registeContest(Long contestId) {
        Contest contest = contestMapper.getContestById(contestId);
        if(contest == null){
            throw new RuntimeException("比赛不存在");
        }
        contestMapper.registeContest(contestId, ThreadLocalUtil.getCurrentId());
    }

    /**
     * 添加题目到比赛
     * @param contestId 比赛id
     * @param problemId 题目id
     * @param displayId
     */
    public void addProblemToContest(Long contestId, Integer problemId, String displayId) {
        Contest contest = contestMapper.getContestById(contestId);
        if(contest == null){
            throw new RuntimeException("比赛不存在");
        }
        Problem problem = problemMapper.getById(problemId);// 判断题目是否存在
        if(problem == null){
            throw new RuntimeException("题目不存在");
        }
        // 比赛已经开始或结束，不能添加题目
        if(contest.getStartTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("比赛已经开始或结束，不能添加题目");
        }
        contestMapper.addProblemToContest(contestId, problemId, displayId);
    }

    /**
     * 获取比赛中的题目
     * @param contestId
     * @return
     */
    public List<ContestProblemVO> getContestProblems(Long contestId) {
        Contest contest = contestMapper.getContestById(contestId);
        if(contest == null){
            throw new RuntimeException("比赛不存在");
        }
        if(contest.getStartTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("比赛未开始，不能查看题目");
        }
        return contestMapper.getContestProblems(contestId);
    }
}
