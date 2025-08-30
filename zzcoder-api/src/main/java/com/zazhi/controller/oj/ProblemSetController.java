package com.zazhi.controller.oj;

import com.zazhi.common.pojo.dto.ProblemSetDTO;
import com.zazhi.common.pojo.result.PageResult;
import com.zazhi.common.pojo.result.Result;
import com.zazhi.common.pojo.vo.ProblemSetPageVO;
import com.zazhi.common.pojo.vo.ProblemSetUpdateDTO;
import com.zazhi.common.pojo.vo.ProblemSetVO;
import com.zazhi.service.ProblemSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
@Tag(name = "题单相关接口")
@RequiredArgsConstructor
public class ProblemSetController {
    private final ProblemSetService problemSetService;

    @Operation(summary = "添加题单")
    @PostMapping()
    @RequiresAuthentication
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
    @RequiresAuthentication
    public Result<Void> updateProblemSet(
            @Validated @RequestBody ProblemSetUpdateDTO problemSetUpdateDTO,
            @PathVariable("id") Integer id
    ) {
        log.info("修改题单信息, {}, ID: {}", problemSetUpdateDTO, id);
        problemSetService.updateProblemSet(problemSetUpdateDTO, id);
        return Result.success();
    }

    @Operation(summary = "添加题目到题单")
    @PostMapping("/add-problem")
    @RequiresAuthentication
    public Result addProblemToProblemSet(@RequestParam("problemSetId") Integer problemSetId,
                                         @RequestParam("problemId") Integer problemId) {
        log.info("添加题目到题单, problemSetId: {}, problemId: {}", problemSetId, problemId);
        problemSetService.addProblemToProblemSet(problemSetId, problemId);
        return Result.success();
    }

    @Operation(summary = "从题单删除题目")
    @DeleteMapping("/delete-problem")
    @RequiresAuthentication
    public Result deleteProblemFromProblemSet(@RequestParam("problemSetId") Integer problemSetId,
                                              @RequestParam("problemId") Integer problemId) {
        log.info("从题单删除题目, problemSetId: {}, problemId: {}", problemSetId, problemId);
        problemSetService.deleteProblemFromProblemSet(problemSetId, problemId);
        return Result.success();
    }

    @Operation(summary = "删除题单")
    @DeleteMapping("/{id}")
    @RequiresAuthentication
    public Result<Void> deleteProblemSet(@PathVariable("id") Integer id) {
        log.info("删除题单, id: {}", id);
        problemSetService.deleteProblemSet(id);
        return Result.success();
    }
}
