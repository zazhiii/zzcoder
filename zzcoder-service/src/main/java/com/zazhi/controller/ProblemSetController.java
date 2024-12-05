package com.zazhi.controller;

import com.zazhi.dto.ProblemSetDTO;
import com.zazhi.result.Result;
import com.zazhi.service.ProblemSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/12/5
 * @description: 题单相关接口
 */
@RestController
@RequestMapping("/api/problem-set")
@Validated
@Slf4j
@Tag(name = "题单相关接口")
public class ProblemSetController {

    @Autowired
    ProblemSetService problemSetService;

    @Operation(summary = "添加题单")
    @PostMapping()
    public Result addProblemSet(@RequestBody ProblemSetDTO problemSetDTO) {
        log.info("添加题单, {}", problemSetDTO);
        problemSetService.addProblemSet(problemSetDTO);
        return Result.success();
    }

    @Operation(summary = "修改题单信息")
    @PutMapping()
    public Result updateProblemSet(@RequestBody ProblemSetDTO problemSetDTO) {
        log.info("修改题单信息, {}", problemSetDTO);
        problemSetService.updateProblemSet(problemSetDTO);
        return Result.success();
    }
}
