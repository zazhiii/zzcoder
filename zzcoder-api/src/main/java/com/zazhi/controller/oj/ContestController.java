package com.zazhi.controller.oj;

import com.zazhi.pojo.entity.Contest;
import com.zazhi.pojo.result.Result;
import com.zazhi.service.ContestService;
import com.zazhi.pojo.vo.ContestProblemVO;
import com.zazhi.pojo.vo.ContestVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zazhi
 * @date 2025/2/11
 * @description:
 */
@RestController
@RequestMapping("/api/contest")
@Slf4j
@Tag(name = "比赛模块")
public class ContestController {

    @Autowired
    ContestService contestService;

    @GetMapping("/list")
    @Operation(summary = "获取比赛列表")
    public Result<List<Contest>> getContest() {
        log.info("获取比赛列表");

        return Result.success(contestService.getPublicContests());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取比赛详细信息")
    public Result<ContestVO> getContestDetail(@PathVariable Long id) {
        log.info("获取比赛详细信息");

        return Result.success(contestService.getContestDetail(id));
    }

    @PostMapping("/register")
    @Operation(summary = "报名比赛")
    @RequiresAuthentication
    public Result registerContest(Long contestId) {
        log.info("报名比赛");

        contestService.registerContest(contestId);
        return Result.success();
    }

    // 查询比赛中的题目
    @GetMapping("/problem")
    @Operation(summary = "获取比赛题目")
    public Result<List<ContestProblemVO>> getContestProblems(Long contestId) {
        log.info("获取比赛题目");

        return Result.success(contestService.getContestProblems(contestId));
    }

}
