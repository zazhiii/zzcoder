package com.zazhi.judger.service.impl;

import com.zazhi.entity.JudgeTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zazhi
 * @date 2024/11/14
 * @description: 评测服务实现类
 */
@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    /**
     * 处理评测任务
     *
     * @param task
     */
    public void processTask(JudgeTask task) {
        log.info("开始处理评测任务: {}", task);
        // TODO
    }
}
