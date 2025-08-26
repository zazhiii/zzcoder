package com.zazhi.controller.oj;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author lixh
 * @since 2025/8/26 23:06
 */
@RestController
@RequestMapping("/api/judge")
public class JudgeSseController {
    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> testCaseEmitters = new ConcurrentHashMap<>();

    @GetMapping("/subscribe/{taskId}")
    public SseEmitter subscribe(@PathVariable Long taskId) {
        SseEmitter emitter = new SseEmitter(0L); // 不超时
        sseEmitters.put(taskId, emitter);

        emitter.onCompletion(() -> sseEmitters.remove(taskId));
        emitter.onTimeout(() -> sseEmitters.remove(taskId));
        emitter.onError((e) -> sseEmitters.remove(taskId));

        return emitter;
    }

    @GetMapping("/subscribe/{taskId}/{testCaseId}")
    public SseEmitter testCaseSubscribe(@PathVariable("taskId") Long taskId,
                                        @PathVariable("testCaseId") Long testCaseId) {
        SseEmitter emitter = new SseEmitter(0L); // 不超时
        String key = taskId + "-" + testCaseId;
        testCaseEmitters.put(key, emitter);
        emitter.onCompletion(() -> testCaseEmitters.remove(key));
        emitter.onTimeout(() -> testCaseEmitters.remove(key));
        emitter.onError((e) -> testCaseEmitters.remove(key));

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
                if (!"PENDING".equals(status) && !"JUDGING".equals(status)) {
                    // 如果状态是最终状态，关闭连接并移除 emitter
                    emitter.complete();
                    sseEmitters.remove(taskId);
                }
            } catch (IOException e) {
                emitter.completeWithError(e);
                sseEmitters.remove(taskId);
            }
        }
    }

    public void sendTestCaseStatus(Long submissionId, Long testCaseId, String status) {
        SseEmitter emitter = testCaseEmitters.get(submissionId + "-" + testCaseId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("status")
                        .data(status));
                if (!"PENDING".equals(status) && !"JUDGING".equals(status)) {
                    // 如果状态是最终状态，关闭连接并移除 emitter
                    emitter.complete();
                    testCaseEmitters.remove(submissionId + "-" + testCaseId);
                }
            } catch (IOException e) {
                emitter.completeWithError(e);
                testCaseEmitters.remove(submissionId + "-" + testCaseId);
            }
        }
    }
}
