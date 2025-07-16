//package com.zazhi.judger.listener;
//
//import com.zazhi.judger.common.pojo.JudgeResult;
//import com.zazhi.judger.common.pojo.JudgeTask;
//import com.zazhi.judger.sandbox.JavaSandBox;
//import com.zazhi.judger.common.utils.MessageQueueUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.ExecutorService;
//
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class JudgeTaskListener {
//
////    private final JavaSandBox javaSandBox;
//
//    private final ExecutorService threadPool;
//
//    @RabbitListener(queues = "judge_task_queue")
//    public void receiveJudgeTask(JudgeTask task) {
//
////        threadPool.execute(() -> {
////            javaSandBox.processTask(task);
////        });
//
//    }
//}
