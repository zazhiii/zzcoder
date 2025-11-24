package com.zazhi.zzcoder.controller.oj;

import com.zazhi.zzcoder.common.pojo.dto.ProblemSetDTO;
import com.zazhi.zzcoder.common.pojo.result.PageResult;
import com.zazhi.zzcoder.common.pojo.result.Result;
import com.zazhi.zzcoder.common.pojo.vo.ProblemSetPageVO;
import com.zazhi.zzcoder.common.pojo.vo.ProblemSetUpdateDTO;
import com.zazhi.zzcoder.common.pojo.vo.ProblemSetVO;
import com.zazhi.zzcoder.service.ProblemSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/12/5
 * @description: 题单相关接口
 */
@RestController
@RequestMapping("/api/problem-set")
@Slf4j
@Tag(name = "题单")
@RequiredArgsConstructor
public class ProblemSetController {
    private final ProblemSetService problemSetService;

    @Operation(summary = "添加题单")
    @PostMapping()
    public Result<Void> addProblemSet(@RequestBody ProblemSetDTO problemSetDTO) {
        log.info("添加题单, {}", problemSetDTO);
        problemSetService.addProblemSet(problemSetDTO);
        return Result.success();
    }

    @Operation(summary = "分页条件查询题单")
    @GetMapping("/page")
    public Result<PageResult<ProblemSetPageVO>> pageProblemSet(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        log.info("分页条件查询题单, page: {}, size: {}, title: {}, status: {}", page, size, title, status);
        return Result.success(problemSetService.pageProblemSet(page, size, title, status));
    }

    @Operation(summary = "获取题单详细信息")
    @GetMapping("/{id}")
    public Result<ProblemSetVO> getProblemSetDetail(@PathVariable("id") Integer id) {
        log.info("获取题单详细信息, id: {}", id);
        return Result.success(problemSetService.getProblemSetDetail(id));
    }

    @Operation(summary = "修改题单信息")
    @PutMapping("/{id}")
    public Result<Void> updateProblemSet(
            @Validated @RequestBody ProblemSetUpdateDTO problemSetUpdateDTO,
            @PathVariable("id") Integer id
    ) {
        log.info("修改题单信息, {}, ID: {}", problemSetUpdateDTO, id);
        problemSetService.updateProblemSet(problemSetUpdateDTO, id);
        return Result.success();
    }

    @Operation(summary = "从题单删除内部题目")
    @DeleteMapping("/internal")
    public Result<Void> deleteInternalProblem(@RequestParam("problemSetId") Integer problemSetId,
                                              @RequestParam("problemId") Integer problemId) {
        log.info("从题单删除题目, problemSetId: {}, problemId: {}", problemSetId, problemId);
        problemSetService.deleteInternalProblem(problemSetId, problemId);
        return Result.success();
    }

    @Operation(summary = "从题单删除外部题目")
    @DeleteMapping("/external")
    public Result<Void> deleteExternalProblem(@RequestParam("problemSetId") Integer problemSetId,
                                              @RequestParam("problemId") Integer problemId) {
        log.info("从题单删除外部题目, problemSetId: {}, problemId: {}", problemSetId, problemId);
        problemSetService.deleteExternalProblem(problemSetId, problemId);
        return  Result.success();
    }

    @Operation(summary = "删除题单")
    @DeleteMapping("/{id}")
    public Result<Void> deleteProblemSet(@PathVariable("id") Integer id) {
        log.info("删除题单, id: {}", id);
        problemSetService.deleteProblemSet(id);
        return Result.success();
    }

    @Operation(summary = "添加本站题目到题单")
    @PostMapping("/add-problem/internal")
    public Result<Void> addInternalProblem(@RequestParam("problemSetId") Integer problemSetId,
                                           @RequestParam("problemId") Integer problemId) {
        log.info("添加题目到题单, problemSetId: {}, problemId: {}", problemSetId, problemId);
        problemSetService.addInternalProblem(problemSetId, problemId);
        return Result.success();
    }
}
