package com.zazhi.controller.oj;

import com.zazhi.common.pojo.dto.JudgeDTO;
import com.zazhi.common.pojo.dto.SubmissionQueryDTO;
import com.zazhi.common.pojo.result.PageResult;
import com.zazhi.common.pojo.result.Result;
import com.zazhi.common.pojo.vo.UserProblemSubmissionVO;
import com.zazhi.service.JudgeService;
import com.zazhi.common.pojo.vo.SubmissionInfoVO;
import com.zazhi.common.pojo.vo.SubmissionPageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zazhi
 * @date 2024/11/12
 * @description: 判题模块接口
 */
@RestController
@RequestMapping("/api/judge")
@Slf4j
@Tag(name = "判题模块接口")
@RequiredArgsConstructor
public class JudgeController {
    private final JudgeService judgeService;

    @PostMapping("/submit")
    @Operation(summary = "提交代码")
    @RequiresAuthentication
    public Result<Long> submitCode(@RequestBody @Validated JudgeDTO JudgeDTO) {
        return Result.success(judgeService.submitCode(JudgeDTO));
    }

    @GetMapping("/submission/{submitId}")
    @Operation(summary = "获取提交记录详情")
    public Result<SubmissionInfoVO> getSubmissionInfo(@PathVariable Long submitId) {
        return Result.success(judgeService.getSubmissionInfo(submitId));
    }

    @PostMapping("/submission")
    @Operation(summary = "获取提交记录")
    public Result<PageResult<SubmissionPageVO>> getSubmissions(@RequestBody SubmissionQueryDTO submissionQueryDTO) {
        return Result.success(judgeService.getSubmissions(submissionQueryDTO));
    }

    @Operation(summary = "获取某题目用户的所有提交记录")
    @GetMapping("/submission/user/{problemId}")
    public Result<List<UserProblemSubmissionVO>> getUserSubmissionsByProblemId(@PathVariable Integer problemId) {
        return Result.success(judgeService.getUserSubmissionsByProblemId(problemId));
    }

    @Operation(summary = "查询题目所有测试数据id")
    @GetMapping("/test-case-ids/{problemId}")
    public Result<List<Long>> getTestCaseIdsByProblemId(@PathVariable Integer problemId) {
        return Result.success(judgeService.getTestCaseIdsByProblemId(problemId));
    }
}
