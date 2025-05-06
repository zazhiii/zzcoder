package com.zazhi.listener;

import com.zazhi.pojo.entity.JudgeResult;
import com.zazhi.service.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class JudgeResultListener {

    @Autowired
    JudgeService judgeService;

    @RabbitListener(queues = "judge_result_queue")
    public void receiveJudgeResult(JudgeResult judgeResult) {
        log.info("JUDGE RESULT: {}", judgeResult);
        judgeService.updateSubmission(judgeResult);
    }
}
