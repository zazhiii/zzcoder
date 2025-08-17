package com.zazhi.judger.listener;

import com.zazhi.common.enums.JudgeStatus;
import com.zazhi.common.pojo.entity.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import com.zazhi.judger.common.utils.MessageQueueUtil;
import com.zazhi.judger.service.JudgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;


@Component
@Slf4j
@RequiredArgsConstructor
public class JudgeTaskListener {
    private final JudgeService judgeService;

    private final ExecutorService threadPool;

    private final MessageQueueUtil messageQueueUtil;

    @RabbitListener(queues = "judge_task_queue")
    public void receiveJudgeTask(JudgeTask task) {
        threadPool.execute(() -> {
            log.info("开始执行任务：{}", task.getTaskId());

            JudgeResult result = new JudgeResult();
            result.setTaskId(task.getTaskId());
            result.setStatus(JudgeStatus.JUDGING);
            messageQueueUtil.sendJudgeResult(result);

            result = judgeService.judge(task);
            messageQueueUtil.sendJudgeResult(result);
        });
    }
}
