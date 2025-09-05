package com.zazhi.controller.admin;

import com.zazhi.common.pojo.dto.ContestDTO;
import com.zazhi.common.pojo.entity.Contest;
import com.zazhi.common.pojo.result.Result;
import com.zazhi.service.ContestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zazhi.common.constants.PermissionConstants.*;

/**
 * @author zazhi
 * @date 2025/2/10
 * @description: 管理员比赛相关接口
 */
@RestController
@RequestMapping("/api/admin/contest")
@Slf4j
@Tag(name = "比赛管理")
public class AdminContestController {

    @Autowired
    ContestService contestService;

    @PostMapping()
    @Operation(summary = "添加比赛")
    @RequiresAuthentication
    @RequiresPermissions(CONTEST_CREATE)
    public Result createContest(@RequestBody ContestDTO contestDTO) {
        log.info("添加比赛：{}", contestDTO);
        contestService.createContest(contestDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "获取我创建的比赛列表")
    @RequiresAuthentication
    @RequiresPermissions(CONTEST_LIST)
    public Result<List<Contest>> getContest() {
        log.info("获取我创建的比赛列表");

        return Result.success(contestService.getContestList());
    }

    @PutMapping()
    @Operation(summary = "修改比赛")
    @RequiresAuthentication
    @RequiresPermissions(CONTEST_UPDATE)
    public Result updateContest(@RequestBody ContestDTO contestDTO) {
        log.info("修改比赛：{}", contestDTO);

        contestService.updateContest(contestDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除比赛")
    @RequiresAuthentication
    @RequiresPermissions(CONTEST_DELETE)
    public Result deleteContest(@PathVariable Long id) {
        log.info("删除比赛：{}", id);

        contestService.deleteContest(id);
        return Result.success();
    }

    @PostMapping("/add-problem-to-contest")
    @Operation(summary = "添加题目到比赛")
    @RequiresAuthentication
    @RequiresPermissions(CONTEST_ADD_PROBLEM_TO_CONTEST)
    public Result addProblemToContest(Long contestId, Integer problemId, String displayId) {
        log.info("添加题目到比赛：{}", contestId);

        contestService.addProblemToContest(contestId, problemId, displayId);
        return Result.success();
    }



//    // 广播消息
//    @PostMapping("/broadcast")
//    @Operation(summary = "广播消息")
//    @RequiresAuthentication
////    @RequiresPermissions("contest:broadcast")
//    public Result broadcast(Integer contestId, String message) {
//        return Result.success();
//    }

}
