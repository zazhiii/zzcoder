package com.zazhi.controller.admin;

import com.zazhi.entity.Problem;
import com.zazhi.entity.TestCase;
import com.zazhi.result.Result;
import com.zazhi.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/12/8
 * @description: 题目相关接口
 */
@RestController
@RequestMapping("/api/admin/problem")
//@Validated
@Slf4j
@Tag(name = "题目管理")
public class AdminProblemController {

    @Autowired
    ProblemService problemService;

    @PostMapping()
    @Operation(summary = "添加题目")
    @RequiresAuthentication
    @RequiresPermissions("problem:add")
    public Result addProblem(@RequestBody Problem problem) {
        log.info("添加题目：{}", problem);
        problemService.addProblem(problem);
        return Result.success();
    }

    @PutMapping()
    @Operation(summary = "修改题目基本信息")
    @RequiresAuthentication
    @RequiresPermissions("problem:update")
    public Result update(@RequestBody Problem problem){
        log.info("修改题目：{}", problem);
        problemService.update(problem);
        return Result.success();
    }

//    @DeleteMapping()
//    @Operation(summary = "删除题目") // TODO 权限管理
//    // TODO 删除题目和标签、测试用例、提交记录、...的关联
//    public Result delete(Integer id){
//        log.info("删除题目：{}", id);
//        problemService.deleteProblemWithTags(id);
//        return Result.success();
//    }

    @PostMapping("/add-tag-to-problem")
    @Operation(summary = "为题目添加标签")
    @RequiresAuthentication
    @RequiresPermissions("problem:add-tag")
    public Result addTagToProblem(Integer problemId, Integer tagId){
        log.info("添加标签到题目：{}, {}", problemId, tagId);
        problemService.addTagToProblem(problemId, tagId);
        return Result.success();
    }

    @DeleteMapping("/delete-tag-from-problem")
    @Operation(summary = "删除题目上的标签")
    @RequiresAuthentication
    @RequiresPermissions("problem:delete-tag")
    public Result deleteTagFromProblem(Integer problemId, Integer tagId){
        log.info("删除题目{}上的标签：{}", problemId, tagId);
        problemService.deleteTagFromProblem(problemId, tagId);
        return Result.success();
    }

    @PostMapping("/add-test-case")
    @Operation(summary = "为题目添加测试用例")
    @RequiresAuthentication
    @RequiresPermissions("problem:add-test-case")
    public Result addTestCase(@RequestBody TestCase testCase){
        log.info("添加测试用例：{}", testCase);
        problemService.addTestCase(testCase);
        return Result.success();
    }

    @DeleteMapping("/delete-test-case")
    @Operation(summary = "删除题目的测试用例")
    @RequiresAuthentication
    @RequiresPermissions("problem:delete-test-case")
    public Result deleteTestCase(Integer id){
        log.info("删除测试用例：{}", id);
        problemService.deleteTestCase(id);
        return Result.success();
    }
}
