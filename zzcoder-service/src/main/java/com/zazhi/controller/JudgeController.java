package com.zazhi.controller;

import com.zazhi.dto.JudgeDTO;
import com.zazhi.dto.SubmissionQueryDTO;
import com.zazhi.entity.Submission;
import com.zazhi.result.PageResult;
import com.zazhi.result.Result;
import com.zazhi.service.JudgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
    public Result<Long> submitCode(@RequestBody @Validated JudgeDTO JudgeDTO) {
        return Result.success(judgeService.submitCode(JudgeDTO));
    }

//    @GetMapping("/submission/{submitId}")

    @PostMapping("/submission")
    @Operation(summary = "获取提交记录")
    public Result<PageResult<Submission>> getSubmissions(@RequestBody SubmissionQueryDTO submissionQueryDTO) {
        return Result.success(judgeService.getSubmissions(submissionQueryDTO));
    }


}
