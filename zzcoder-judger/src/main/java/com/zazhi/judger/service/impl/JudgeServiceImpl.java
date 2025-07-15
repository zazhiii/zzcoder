package com.zazhi.judger.service.impl;

import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import com.zazhi.judger.service.JudgeService;
import org.springframework.stereotype.Service;

/**
 * @author lixinhuan
 * @date 2025/7/15
 * @description: JudgeServiceImpl 类实现了 JudgeService 接口，提供判题服务的具体实现
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    /**
     * 执行判题任务的方法
     * @param judgeTask
     * @return
     */
    @Override
    public JudgeResult judge(JudgeTask judgeTask) {
        return null;
    }
}
