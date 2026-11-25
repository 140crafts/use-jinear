package co.jinear.core.service.team.workflow;

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
public class TeamWorkflowStatusLockService {
    private final LockService lockService;

    @Retryable(includes = LockedException.class, maxRetries = 7, delay = 200, multiplier = 2, maxDelay = 3000)
    public void lockTeamWorkflow(String teamId) {
        log.info("Lock team workflow status has started for teamId: {}", teamId);
        lockService.lock(teamId, LockSourceType.TEAM_WORKFLOW_STATUS);
        log.info("Lock team workflow status  has ended for teamId: {}", teamId);
    }

    public void unlockTeamWorkflow(String teamId) {
        log.info("Unlock team workflow status  has started for teamId: {}", teamId);
        lockService.unlock(teamId, LockSourceType.TEAM_WORKFLOW_STATUS);
        log.info("Unlock team workflow status  has ended for teamId: {}", teamId);
    }
}
