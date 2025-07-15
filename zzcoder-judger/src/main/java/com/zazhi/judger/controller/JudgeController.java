package com.zazhi.judger.controller;

import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import com.zazhi.judger.common.pojo.Result;
import com.zazhi.judger.service.JudgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lixinhuan
 * @date 2025/7/10
 * @description: JudgeController 类用于处理判题请求的控制器
 */
@RestController
@RequestMapping("api/judge")
@RequiredArgsConstructor
public class JudgeController {

    private final JudgeService judgeService;

    @PostMapping
    public Result<JudgeResult> judge(@RequestBody JudgeTask judgeTask){
        return Result.success(judgeService.judge(judgeTask));
    }

}
