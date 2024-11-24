package com.zazhi.controller;

import com.zazhi.dto.ProblemDTO;
import com.zazhi.entity.Problem;
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

    @PostMapping()
    @Operation(summary = "添加题目")
    // TODO 权限验证
    public Result addProblem(@RequestBody Problem problem) {
        log.info("添加题目：{}", problem);
        problemService.addProblem(problem);
        return Result.success();
    }

    @PostMapping("/list")
    @Operation(summary = "题目条件分页查询")
    public Result<PageResult<ProblemVO>> list(@RequestBody ProblemQueryDTO problemQueryDTO){
        log.info("分页查询题目，{}", problemQueryDTO);
        return Result.success(problemService.page(problemQueryDTO));
    }

    @PutMapping()
    @Operation(summary = "修改题目基本信息") // TODO 权限管理
    public Result update(@RequestBody Problem problem){
        log.info("修改题目：{}", problem);
        problemService.update(problem);
        return Result.success();
    }

    @DeleteMapping()
    @Operation(summary = "删除题目") // TODO 权限管理
    // TODO 删除题目和标签、测试用例、提交记录、...的关联
    public Result delete(Integer id){
        log.info("删除题目：{}", id);
        problemService.deleteProblemWithTags(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查看题目详细信息")
    public Result<ProblemInfoVO> getProblemInfo(@PathVariable Integer id){
        log.info("查看题目：{}", id);
        return Result.success(problemService.getProblemInfo(id));
    }

    @PostMapping("/add-tag-to-problem")
    @Operation(summary = "为题目添加标签")// TODO 权限管理
    public Result addTagToProblem(Integer problemId, @RequestParam List<Integer> tagIds){
        log.info("添加标签到题目：{}, {}", problemId, tagIds);
        problemService.addTagToProblem(problemId, tagIds);
        return Result.success();
    }

    @DeleteMapping("/delete-tag-from-problem")
    @Operation(summary = "删除题目上的标签")// TODO 权限管理
    public Result deleteTagFromProblem(Integer problemId, Integer tagId){
        log.info("删除题目{}上的标签：{}", problemId, tagId);
        problemService.deleteTagFromProblem(problemId, tagId);
        return Result.success();
    }
}
