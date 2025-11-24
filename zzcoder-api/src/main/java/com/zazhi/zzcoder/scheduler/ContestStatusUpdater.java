package com.zazhi.zzcoder.scheduler;

import com.zazhi.zzcoder.common.enums.ContestStatus;
import com.zazhi.zzcoder.common.pojo.entity.Contest;
import com.zazhi.zzcoder.service.ContestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Slf4j
public class ContestStatusUpdater {

    @Autowired
    private ContestService contestService;

    @Scheduled(cron = "0 * * * * ?")
    public void updateContestStatus() {
        Instant now = Instant.now();
        List<Contest> contests = contestService.getActiveOrUpcomingContests();
        for (Contest contest : contests) {
            Instant startTime = contest.getStartTime();
            Instant endTime = startTime.plus(contest.getDuration(), ChronoUnit.MINUTES);
            if (endTime.isBefore(now) && contest.getStatus() != ContestStatus.ENDED) {
                contestService.updateContestStatus(contest.getId(), ContestStatus.ENDED);
            }
            if (startTime.isBefore(now) && contest.getStatus() != ContestStatus.ACTIVE) {
                contestService.updateContestStatus(contest.getId(), ContestStatus.ACTIVE);
            }
        }
    }
}
