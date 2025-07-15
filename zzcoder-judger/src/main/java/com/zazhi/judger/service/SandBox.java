package com.zazhi.judger.service;

import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;

public interface SandBox {

    /**
     * 处理判题任务
     * @param task 判题任务
     * @return 判题结果
     */
    JudgeResult judge(JudgeTask task);

    void saveCode(String code, String workDir, String fileName);
}
