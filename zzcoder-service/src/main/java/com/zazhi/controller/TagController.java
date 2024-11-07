package com.zazhi.controller;

import com.zazhi.result.Result;
import com.zazhi.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zazhi
 * @date 2024/11/7
 * @description: 题目标签相关接口
 */
@RestController
@RequestMapping("/api/tag")
@Validated
@Slf4j
@Tag(name = "题目标签相关接口")
public class TagController {

    @Autowired
    TagService tagService;

    @PostMapping
    @Operation(summary = "新增标签")
    public Result add(String name){
        log.info("添加标签：{}", name);
        tagService.insert(name);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签")
    public Result delete(@PathVariable Integer id){
        log.info("删除标签：{}", id);
        tagService.deletById(id);
        return Result.success();
    }
}
