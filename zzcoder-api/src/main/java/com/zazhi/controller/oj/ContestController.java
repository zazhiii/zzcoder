package com.zazhi.controller.oj;

import com.zazhi.common.pojo.result.PageResult;
import com.zazhi.common.pojo.result.Result;
import com.zazhi.common.pojo.vo.ContestPageVO;
import com.zazhi.common.pojo.vo.UpcomingContestVO;
import com.zazhi.service.ContestService;
import com.zazhi.common.pojo.vo.ContestProblemVO;
import com.zazhi.common.pojo.vo.ContestVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;

    @GetMapping("/list")
    @Operation(summary = "获取比赛列表")
    public Result<PageResult<ContestPageVO>> getContest(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "contestStatus", required = false) Integer contestStatus,
            @RequestParam(value = "type", required = false) Integer type
    ) {
        log.info("获取比赛列表");

        return Result.success(contestService.page(pageNum, pageSize, keyword, contestStatus, type));
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

    @GetMapping("/clist")
    @Operation(summary = "从clist获取即将开始的比赛")
    public Result<List<UpcomingContestVO>> getUpcomingContestsFromClist(
            @RequestParam Boolean upcoming,
            @RequestParam String resourceRegex
    ){
        log.info("从clist获取即将开始的比赛");
        return Result.success(contestService.getUpcomingContestsFromClist(upcoming, resourceRegex));
    }

}
