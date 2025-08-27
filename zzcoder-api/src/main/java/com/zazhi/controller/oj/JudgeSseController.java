package com.zazhi.controller.oj;

import com.zazhi.common.pojo.entity.TestCase;
import com.zazhi.common.pojo.entity.TestCaseResult;
import com.zazhi.service.JudgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author lixh
 * @since 2025/8/26 23:06
 */
@RestController
@RequestMapping("/api/judge")
@RequiredArgsConstructor
public class JudgeSseController {
    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    private final Map<Long, AtomicInteger> testCaseCount = new ConcurrentHashMap<>();

    private final Map<Long, Boolean> isCompleted = new ConcurrentHashMap<>();

    private final JudgeService judgeService;

    @GetMapping("/subscribe/{taskId}")
    public SseEmitter subscribe(@PathVariable Long taskId) {
        SseEmitter emitter = new SseEmitter(0L); // 不超时
        sseEmitters.put(taskId, emitter);

        // 记录提交的测试用例数量 ( taskId 就是 submissionId )
        Integer count = judgeService.getTestCaseCountBySubmissionId(taskId);
        testCaseCount.put(taskId, new AtomicInteger(count));
        isCompleted.put(taskId, false);

        emitter.onCompletion(() -> sseEmitters.remove(taskId));
        emitter.onTimeout(() -> sseEmitters.remove(taskId));
        emitter.onError((e) -> sseEmitters.remove(taskId));

        return emitter;
    }

    // 向前端推送消息
    public void sendStatus(Long taskId, String status) {
        SseEmitter emitter = sseEmitters.get(taskId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("status")
                        .data(status));

                // 最终状态 且没有剩余测试用例需要通知时，关闭连接
                if (!"PENDING".equals(status) && !"JUDGING".equals(status)) {
                    isCompleted.put(taskId, true); // 标记为已完成
                    if(testCaseCount.get(taskId).get() == 0){
                        // 如果状态是最终状态，关闭连接并移除 emitter
                        emitter.complete();
                        sseEmitters.remove(taskId);
                        isCompleted.remove(taskId);
                        testCaseCount.remove(taskId);
                    }
                }
            } catch (IOException e) {
                emitter.completeWithError(e);
                sseEmitters.remove(taskId);
            }
        }
    }

    public void sendTestCaseStatus(Long submissionId, TestCaseResult caseResult) {
        SseEmitter emitter = sseEmitters.get(submissionId);
        if (emitter != null) {
            try {
                HashMap<String, Object> map = new HashMap<>();
                map.put("index", caseResult.getIndex());
                map.put("testCaseId", caseResult.getTestCaseId());
                map.put("status", caseResult.getStatus());
                map.put("timeUsed", caseResult.getTimeUsed());
                map.put("memoryUsed", caseResult.getMemoryUsed());
                map.put("errorMessage", caseResult.getErrorMessage());

                emitter.send(SseEmitter.event()
                        .name("testCaseUpdate")
                        .data(map));

                // 减少剩余测试用例数量
                testCaseCount.get(submissionId).decrementAndGet();
                // 如果所有测试用例都已处理，关闭连接并移除 key
                if(testCaseCount.get(submissionId).get() == 0 && isCompleted.get(submissionId)) {
                    emitter.complete();
                    sseEmitters.remove(submissionId);
                    testCaseCount.remove(submissionId);
                    isCompleted.remove(submissionId);
                }
            } catch (IOException e) {
                emitter.completeWithError(e);
                sseEmitters.remove(submissionId);
            }
        }
    }
}
