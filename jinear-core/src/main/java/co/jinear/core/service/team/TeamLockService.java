package co.jinear.core.service.team;

import co.jinear.core.exception.lock.LockedException;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamLockService {

    private final LockService lockService;

    @Retryable(includes = LockedException.class, maxRetries = 7, delay = 200, multiplier = 2, maxDelay = 3000)
    public void lockTeamForTaskInitialization(String teamId) {
        log.info("Lock team for task initialization has started for topicId: {}", teamId);
        lockService.lock(teamId, LockSourceType.TEAM_TASK_INIT);
        log.info("Lock team for task initialization has ended for topicId: {}", teamId);
    }

    public void unlockTeamForTaskInitialization(String teamId) {
        log.info("Unlock team for task initialization has started for topicId: {}", teamId);
        lockService.unlock(teamId, LockSourceType.TEAM_TASK_INIT);
        log.info("Unlock team for task initialization has ended for topicId: {}", teamId);
    }
}
