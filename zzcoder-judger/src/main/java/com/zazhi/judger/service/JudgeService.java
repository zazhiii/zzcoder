package com.zazhi.judger.service;

import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import org.jvnet.hk2.annotations.Service;

/**
 * @author lixinhuan
 * @date 2025/7/15
 * @description: JudgeService 接口定义了判题服务的基本操作
 */
public interface JudgeService {
    JudgeResult judge(JudgeTask judgeTask);
}
