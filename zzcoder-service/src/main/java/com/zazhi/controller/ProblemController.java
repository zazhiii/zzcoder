package com.zazhi.controller;

import com.zazhi.dto.ProblemDTO;
import com.zazhi.result.Result;
import com.zazhi.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zazhi
 * @date 2024/11/6
 * @description: 题目相关接口
 */
@RestController
@RequestMapping("/api/problem")
@Validated
@Slf4j
@Tag(name = "题目相关接口")
public class ProblemController {

    @Autowired
    ProblemService problemService;

    @PostMapping()
    @Operation(summary = "添加题目")
    public Result addProblem(@RequestBody ProblemDTO problemDTO) {
        log.info("添加题目：{}", problemDTO);
        problemService.addProblem(problemDTO);
        return Result.success();
    }
}
