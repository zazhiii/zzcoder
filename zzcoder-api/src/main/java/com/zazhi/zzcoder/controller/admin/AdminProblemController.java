package com.zazhi.zzcoder.controller.admin;

import com.zazhi.zzcoder.common.pojo.entity.Problem;
import com.zazhi.zzcoder.common.pojo.entity.TestCase;
import com.zazhi.zzcoder.common.pojo.result.Result;
import com.zazhi.zzcoder.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.zazhi.zzcoder.common.constants.PermissionConstants.*;

/**
 * @author zazhi
 * @date 2024/12/8
 * @description: 题目相关接口
 */
@RestController
@RequestMapping("/api/admin/problem")
@Slf4j
@Tag(name = "题目管理")
public class AdminProblemController {

    @Autowired
    ProblemService problemService;

    @PostMapping()
    @Operation(summary = "添加题目")
    public Result addProblem(@RequestBody Problem problem) {
        log.info("添加题目：{}", problem);
        problemService.addProblem(problem);
        return Result.success();
    }

    @PutMapping()
    @Operation(summary = "修改题目基本信息")
    public Result update(@RequestBody Problem problem){
        log.info("修改题目：{}", problem);
        problemService.update(problem);
        return Result.success();
    }

//    @DeleteMapping()
//    @Operation(summary = "删除题目")
//    删除题目和标签、测试用例、提交记录、...的关联
//    public Result delete(Integer id){
//        log.info("删除题目：{}", id);
//        problemService.deleteProblemWithTags(id);
//        return Result.success();
//    }

    @PutMapping("/add-tag-to-problem")
    @Operation(summary = "为题目添加标签")
    public Result<Void> addTagToProblem(Integer problemId, Integer tagId){
        log.info("添加标签到题目：{}, {}", problemId, tagId);
        problemService.addTagToProblem(problemId, tagId);
        return Result.success();
    }

    @DeleteMapping("/delete-tag-from-problem")
    @Operation(summary = "删除题目上的标签")
    public Result deleteTagFromProblem(Integer problemId, Integer tagId){
        log.info("删除题目{}上的标签：{}", problemId, tagId);
        problemService.deleteTagFromProblem(problemId, tagId);
        return Result.success();
    }

    @PostMapping("/add-test-case")
    @Operation(summary = "为题目添加测试用例")
    public Result addTestCase(@RequestBody TestCase testCase){
        log.info("添加测试用例：{}", testCase);
        problemService.addTestCase(testCase);
        return Result.success();
    }

    @DeleteMapping("/delete-test-case")
    @Operation(summary = "删除题目的测试用例")
    public Result deleteTestCase(Integer id){
        log.info("删除测试用例：{}", id);
        problemService.deleteTestCase(id);
        return Result.success();
    }
}