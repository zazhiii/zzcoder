package com.zazhi.controller.oj;

import com.zazhi.entity.TestCase;
import com.zazhi.vo.ProblemInfoVO;
import com.zazhi.dto.ProblemQueryDTO;
import com.zazhi.result.PageResult;
import com.zazhi.result.Result;
import com.zazhi.service.ProblemService;
import com.zazhi.vo.ProblemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/list")
    @Operation(summary = "题目条件分页查询")
    public Result<PageResult<ProblemVO>> list(@RequestBody ProblemQueryDTO problemQueryDTO){
        log.info("分页查询题目，{}", problemQueryDTO);
        return Result.success(problemService.page(problemQueryDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查看题目详细信息")
    public Result<ProblemInfoVO> getProblemInfo(@PathVariable Integer id){
        log.info("查看题目：{}", id);
        return Result.success(problemService.getProblemInfo(id));
    }

    @GetMapping("/test-cases")
    @Operation(summary = "获取题目的测试用例")
    public Result<List<TestCase>> getTestCases(Integer problemId){
        log.info("获取题目{}的测试用例", problemId);
        return Result.success(problemService.getTestCases(problemId));
    }


}
