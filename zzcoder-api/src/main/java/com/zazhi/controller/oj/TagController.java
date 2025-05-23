package com.zazhi.controller.oj;

import com.zazhi.pojo.result.Result;
import com.zazhi.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.zazhi.pojo.entity.ProblemTag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zazhi.common.constant.PermissionConstants.PROBLEM_TAG_ADD;

/**
 * @author zazhi
 * @date 2024/11/7
 * @description: 题目标签相关接口
 */
@RestController
@RequestMapping("/api/tag")
//@Validated
@Slf4j
@Tag(name = "题目标签相关接口")
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping
    @Operation(summary = "获取所有标签")
    public Result<List<ProblemTag>> list(){
        log.info("获取所有标签");
        return Result.success(tagService.list());
    }

    @PostMapping
    @Operation(summary = "新增标签")
    @RequiresAuthentication
    @RequiresPermissions(PROBLEM_TAG_ADD)
    public Result add(String name){
        log.info("添加标签：{}", name);
        tagService.insert(name);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签") // TODO 权限管理
    public Result delete(@PathVariable Integer id){
        log.info("删除标签：{}", id);
        tagService.deletById(id);
        return Result.success();
    }
}
