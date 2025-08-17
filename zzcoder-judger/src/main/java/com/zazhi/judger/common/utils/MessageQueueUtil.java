package com.zazhi.judger.common.utils;

import com.zazhi.common.pojo.entity.JudgeResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageQueueUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public void sendJudgeTask(JudgeTask judgeTask) {
//        rabbitTemplate.convertAndSend("judge_exchange", "judge_task_routing_key", judgeTask);
//    }

    public void sendJudgeResult(JudgeResult judgeResult) {
        rabbitTemplate.convertAndSend("judge_exchange", "judge_result_routing_key", judgeResult);
    }
}
