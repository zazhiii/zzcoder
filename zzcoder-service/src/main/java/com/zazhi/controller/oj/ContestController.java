package com.zazhi.controller.oj;

import com.zazhi.entity.Contest;
import com.zazhi.result.Result;
import com.zazhi.service.ContestService;
import com.zazhi.vo.ContestVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return Result.success(contestService.getContestList());
    }

    @GetMapping()
    @Operation(summary = "获取比赛详细信息")
    public Result<ContestVO> getContestDetail(Long id) {
        log.info("获取比赛详细信息");

        return Result.success(contestService.getContestDetail(id));
    }

    @PostMapping("/registe")
    @Operation(summary = "报名比赛")
    @RequiresAuthentication
    public Result registeContest(Long contestId) {
        log.info("报名比赛");

        contestService.registeContest(contestId);
        return Result.success();
    }

}
