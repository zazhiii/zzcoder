package com.zazhi.controller.oj;

import com.zazhi.pojo.dto.JudgeDTO;
import com.zazhi.pojo.dto.SubmissionQueryDTO;
import com.zazhi.pojo.result.PageResult;
import com.zazhi.pojo.result.Result;
import com.zazhi.service.JudgeService;
import com.zazhi.pojo.vo.SubmissionInfoVO;
import com.zazhi.pojo.vo.SubmissionPageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/11/12
 * @description: 判题模块接口
 */
@RestController
@RequestMapping("/api/judge")
@Slf4j
@Tag(name = "判题模块接口")
public class JudgeController {

    @Autowired
    JudgeService judgeService;

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


}
