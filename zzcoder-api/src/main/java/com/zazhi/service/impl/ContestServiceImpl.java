package com.zazhi.service.impl;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zazhi.common.enums.ContestStatus;
import com.zazhi.common.utils.ThreadLocalUtil;
import com.zazhi.config.properties.ClistProperties;
import com.zazhi.mapper.ContestMapper;
import com.zazhi.mapper.ProblemMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.pojo.dto.ContestDTO;
import com.zazhi.pojo.entity.ClistContestResponse;
import com.zazhi.pojo.entity.Contest;
import com.zazhi.pojo.entity.Problem;
import com.zazhi.pojo.entity.User;
import com.zazhi.pojo.result.PageResult;
import com.zazhi.pojo.vo.ContestPageVO;
import com.zazhi.pojo.vo.ContestProblemVO;
import com.zazhi.pojo.vo.ContestVO;
import com.zazhi.pojo.vo.UpcomingContestVO;
import com.zazhi.service.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zazhi
 * @date 2025/2/10
 * @description: 管理员比赛相关接口
 */
@Service
@RequiredArgsConstructor
public class ContestServiceImpl implements ContestService {

    private final ContestMapper contestMapper;

    private final UserMapper userMapper;

    private final ProblemMapper problemMapper;

    private final ClistProperties clistProp;

    /**
     * 添加比赛
     * @param contestDTO 比赛信息
     */
    public void createContest(ContestDTO contestDTO) {
        Contest contest = new Contest();
        BeanUtils.copyProperties(contestDTO, contest);
        contest.setStatus(ContestStatus.UPCOMING); // 默认未开始
        Long userId = ThreadLocalUtil.getCurrentId();
        contest.setCreateUser(userId); // 创建人
        // 结束时间
        contest.setEndTime(contest.getStartTime().plus(contest.getDuration(), ChronoUnit.MINUTES));
        contestMapper.insert(contest);
    }

    /**
     * 获取当前用户创建的比赛列表
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
    public void updateContestStatus(Long id, ContestStatus status) {
        contestMapper.updateContestStatus(id, status);
    }

    /**
     * 报名比赛
     * @param contestId
     */
    public void registerContest(Long contestId) {
        Contest contest = contestMapper.getContestById(contestId);
        if(contest == null){
            throw new RuntimeException("比赛不存在");
        }
        contestMapper.registerContest(contestId, ThreadLocalUtil.getCurrentId());
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
        if(contest.getStartTime().isBefore(Instant.now())){
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
        if(contest.getStartTime().isAfter(Instant.now())){
            throw new RuntimeException("比赛未开始，不能查看题目");
        }
        return contestMapper.getContestProblems(contestId);
    }

    /**
     * 获取比赛中和未开始的比赛
     * @return
     */
    public List<Contest> getActiveOrUpcomingContests() {
        return contestMapper.getActiveOrUpcomingContests();
    }

    /**
     * 获取公开比赛列表
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @param contestStatus
     * @param type
     * @return
     */
    @Override
    public PageResult<ContestPageVO> page(Integer pageNum, Integer pageSize, String keyword, Integer contestStatus, Integer type) {
        PageHelper.startPage(pageNum, pageSize);
        Page<ContestPageVO> res = contestMapper.page(keyword, contestStatus, type);
        return new PageResult<>(res.getTotal(), res.getResult());
    }

    /**
     * 从clist获取即将开始的比赛
     * @param upcoming 是否即将开始的比赛
     * @param resourceRegex
     * @return 即将开始的比赛列表
     */
    @Override
    public List<UpcomingContestVO> getUpcomingContestsFromClist(Boolean upcoming, String resourceRegex) {
        String url = clistProp.getUrl();
        Map<String, Object> params = new HashMap<>();
        params.put("upcoming", upcoming);
        params.put("username", clistProp.getUsername());
        params.put("api_key", clistProp.getApiKey());
        params.put("resource__regex", resourceRegex);
        params.put("order_by", "start");
        try {
            // 发送HTTP请求，启用自动重定向
            HttpResponse response = HttpRequest.get(url)
                    .header(Header.AUTHORIZATION, clistProp.getAuthorization())
                    .form(params)
                    .timeout(15000)
                    .setFollowRedirects(true)
                    .execute();
            // 检查响应状态
            if (response.getStatus() != 200 || response.body() == null || response.body().trim().isEmpty()) {
                return List.of();
            }
            ClistContestResponse resp = JSONUtil.toBean(response.body(), ClistContestResponse.class);
            return resp.getObjects();
        } catch (Exception e) {
            // 返回空列表而不是抛出异常
            return List.of();
        }
    }

}
