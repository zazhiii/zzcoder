package com.zazhi.scheduler;

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
        // 获取所有竞赛 TODO: 可以只查出需要处理的比赛，已完成的比赛不需要处理
        List<Contest> contests = contestService.getAllContests();
        
        for (Contest contest : contests) {
            // TODO 状态抽取成枚举
            if (contest.getStartTime().isAfter(now)) {
                // 如果比赛开始时间在当前时间之后，设置为“未开始”
                if (contest.getStatus() != 0) {
                    contestService.updateContestStatus(contest.getId(), 0);
                }
            } else if (contest.getEndTime().isBefore(now)) {
                // 如果比赛结束时间在当前时间之前，设置为“已结束”
                if (contest.getStatus() != 2) {
                    contestService.updateContestStatus(contest.getId(), 2);
                }
            } else {
                // 如果比赛进行中，设置为“比赛中”
                if (contest.getStatus() != 1) {
                    contest.setStatus(1);
                    contestService.updateContestStatus(contest.getId(), 1);
                }
            }
        }
    }
}
