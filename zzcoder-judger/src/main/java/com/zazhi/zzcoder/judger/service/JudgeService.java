package com.zazhi.zzcoder.judger.service;

import com.zazhi.zzcoder.common.pojo.entity.JudgeResult;
import com.zazhi.zzcoder.judger.common.pojo.JudgeTask;

/**
 * @author lixinhuan
 * @date 2025/7/15
 * @description: JudgeService 接口定义了判题服务的基本操作
 */
public interface JudgeService {
    JudgeResult judge(JudgeTask judgeTask);
}
