package com.zazhi.judger.listener;

import com.zazhi.judger.common.pojo.JudgeResult;
import com.zazhi.judger.common.pojo.JudgeTask;
import com.zazhi.judger.service.JudgeService;
import com.zazhi.judger.common.utils.MessageQueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class JudgeTaskListener {

    @Autowired
    JudgeService judgeService;

    @Autowired
    MessageQueueUtil messageQueueUtil;

    @RabbitListener(queues = "judge_task_queue")
    public void receiveJudgeTask(JudgeTask task) {
        log.info("开始处理评测任务: {}", task);
        // 0. 创建评测结果对象
        JudgeResult judgeResult = new JudgeResult();
        judgeResult.setTaskId(task.getTaskId());
        // 1. 设置评测状态为评测中，发送评测结果
        judgeResult.setStatus("Judging");
        messageQueueUtil.sendJudgeResult(judgeResult);
        // 2. 调用评测服务进行评测
        judgeService.processTask(task, judgeResult);
        // 3. 设置评测状态为评测完成，发送评测结果
        judgeResult.setStatus("Completed");
        messageQueueUtil.sendJudgeResult(judgeResult);
        log.info("评测结果: {}", judgeResult);
    }
}
