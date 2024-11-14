package com.zazhi.judger.service.impl;

import com.zazhi.entity.JudgeTask;

public interface JudgeService {

    /**
     * 处理评测任务
     *
     * @param task
     */
    void processTask(JudgeTask task);
}
