package com.zazhi.judger.listener;

import com.zazhi.entity.JudgeTask;
import com.zazhi.judger.service.impl.JudgeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JudgeTaskListener {

    @Autowired
    private JudgeService judgeService;

    @RabbitListener(queues = "judge_task_queue")
    public void receiveJudgeTask(JudgeTask task) {
        judgeService.processTask(task);
    }
}
