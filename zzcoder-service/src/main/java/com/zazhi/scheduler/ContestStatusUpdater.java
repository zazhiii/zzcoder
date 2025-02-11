package com.zazhi.scheduler;

import com.zazhi.constant.ContestStatus;
import com.zazhi.entity.Contest;
import com.zazhi.service.ContestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ContestStatusUpdater {

    @Autowired
    private ContestService contestService;

    // 每分钟检查一次所有竞赛状态
    @Scheduled(cron = "0 * * * * ?") // 每分钟执行一次
    public void updateContestStatus() {
        LocalDateTime now = LocalDateTime.now();
        // 获取所有竞赛
        List<Contest> contests = contestService.getAllContests();
        for (Contest contest : contests) {
            LocalDateTime startTime = contest.getStartTime();
            LocalDateTime endTime = contest.getEndTime();
            if (startTime.isAfter(now)) {
                // 如果比赛开始时间在当前时间之后，设置为“未开始”
                if (contest.getStatus() != ContestStatus.NOT_STARTED.getCode()) {
                    contestService.updateContestStatus(contest.getId(), ContestStatus.NOT_STARTED.getCode());
                }
            } else if (endTime.isBefore(now)) {
                // 如果比赛结束时间在当前时间之前，设置为“已结束”
                if (contest.getStatus() != ContestStatus.ENDED.getCode()) {
                    contestService.updateContestStatus(contest.getId(), ContestStatus.ENDED.getCode());
                }
            } else {
                // 如果比赛进行中，设置为“比赛中”
                if (contest.getStatus() != ContestStatus.IN_PROGRESS.getCode()) {
                    contestService.updateContestStatus(contest.getId(), ContestStatus.IN_PROGRESS.getCode());
                }
            }
        }
    }
}
