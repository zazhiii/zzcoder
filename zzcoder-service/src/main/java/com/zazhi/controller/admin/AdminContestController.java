package com.zazhi.controller.admin;

import com.zazhi.dto.ContestDTO;
import com.zazhi.entity.Contest;
import com.zazhi.result.Result;
import com.zazhi.service.ContestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zazhi
 * @date 2025/2/10
 * @description: 管理员比赛相关接口
 */
@RestController
@RequestMapping("/api/contest")
@Slf4j
@Tag(name = "比赛管理")
public class AdminContestController {

    @Autowired
    ContestService contestService;

    @PostMapping()
    @Operation(summary = "添加比赛")
    @RequiresAuthentication
    @RequiresPermissions("contest:create")
    public Result createContest(@RequestBody ContestDTO contestDTO) {
        log.info("添加比赛：{}", contestDTO);

        contestService.createContest(contestDTO);
        return Result.success();
    }
}
