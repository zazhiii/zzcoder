package com.zazhi.controller.oj;

import com.zazhi.common.pojo.entity.TestCase;
import com.zazhi.common.pojo.vo.ProblemInfoVO;
import com.zazhi.common.pojo.dto.ProblemPageDTO;
import com.zazhi.common.pojo.result.PageResult;
import com.zazhi.common.pojo.result.Result;
import com.zazhi.common.pojo.vo.TagVO;
import com.zazhi.service.ProblemService;
import com.zazhi.common.pojo.vo.ProblemPageVO;
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

    @PostMapping("/page")
    @Operation(summary = "题目条件分页查询")
    public Result<PageResult<ProblemPageVO>> list(@RequestBody ProblemPageDTO problemPageDTO){
        log.info("分页查询题目，{}", problemPageDTO);
        return Result.success(problemService.page(problemPageDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查看题目详细信息")
    public Result<ProblemInfoVO> getProblemInfo(@PathVariable Integer id){
        log.info("查看题目：{}", id);
        return Result.success(problemService.getProblemInfo(id));
    }

    @GetMapping("{problemId}/tags")
    @Operation(summary = "查询题目的标签")
    public Result<List<TagVO>> getProblemTags(@PathVariable Integer problemId){
        log.info("查询题目{}的标签", problemId);
        return Result.success(problemService.getProblemTags(problemId));
    }

    @GetMapping("/test-cases")
    @Operation(summary = "获取题目的测试用例")
    public Result<List<TestCase>> getTestCases(Integer problemId){
        log.info("获取题目{}的测试用例", problemId);
        return Result.success(problemService.getTestCases(problemId));
    }


}
