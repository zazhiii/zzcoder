package com.zazhi.zzcoder.judger.common.utils;

import com.zazhi.zzcoder.common.pojo.entity.JudgeResult;
import com.zazhi.zzcoder.common.pojo.entity.TestCaseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageQueueUtil {
    private final RabbitTemplate rabbitTemplate;

    public void sendJudgeResult(JudgeResult judgeResult) {
        rabbitTemplate.convertAndSend("judge_exchange", "judge_result_routing_key", judgeResult);
    }

    public void sendTestCaseResult(TestCaseResult testCaseResult) {
        rabbitTemplate.convertAndSend("judge_exchange", "test_case_result_routing_key", testCaseResult);
    }
}
