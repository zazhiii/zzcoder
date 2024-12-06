package com.zazhi.controller;

import com.zazhi.dto.ProblemSetDTO;
import com.zazhi.entity.ProblemSet;
import com.zazhi.result.PageResult;
import com.zazhi.result.Result;
import com.zazhi.service.ProblemSetService;
import com.zazhi.vo.ProblemSetVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "分页查询公开题单")
    @GetMapping("/public")
    public Result<PageResult<ProblemSet>> listPublicProblemSet(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                               @RequestParam(value = "title", required = false) String title) {
        log.info("分页查询公开题单, page: {}, size: {}, title: {}", page, size, title);
        return Result.success(problemSetService.listPublicProblemSet(page, size, title));
    }

    @Operation(summary = "查询我的所有题单")
    @GetMapping("/private")
    public Result<List<ProblemSet>> listPrivateProblemSet() {
        return Result.success(problemSetService.listPrivateProblemSet());
    }
    
    @Operation(summary = "添加题目到题单")
    @PostMapping("/add-problem")
    public Result addProblemToProblemSet(@RequestParam("problemSetId") Integer problemSetId,
                                         @RequestParam("problemId") Integer problemId) {
        log.info("添加题目到题单, problemSetId: {}, problemId: {}", problemSetId, problemId);
        problemSetService.addProblemToProblemSet(problemSetId, problemId);
        return Result.success();
    }

    @Operation(summary = "从题单删除题目")
    @DeleteMapping("/delete-problem")
    public Result deleteProblemFromProblemSet(@RequestParam("problemSetId") Integer problemSetId,
                                              @RequestParam("problemId") Integer problemId) {
        log.info("从题单删除题目, problemSetId: {}, problemId: {}", problemSetId, problemId);
        problemSetService.deleteProblemFromProblemSet(problemSetId, problemId);
        return Result.success();
    }

    @Operation(summary = "题单详细信息")
    @GetMapping("/{id}")
    public Result<ProblemSetVO> getProblemSet(@PathVariable("id") Integer id) {
        log.info("题单详细信息, id: {}", id);
        return Result.success(problemSetService.getProblemSet(id));
    }

    @Operation(summary = "删除题单")
    @DeleteMapping("/{id}")
    public Result deleteProblemSet(@PathVariable("id") Integer id) {
        log.info("删除题单, id: {}", id);
        problemSetService.deleteProblemSet(id);
        return Result.success();
    }
}
