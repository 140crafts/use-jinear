package co.jinear.core.service.project.domain;

import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDomainLockService {

    private final LockService lockService;

    public void lockDomain(String domain) {
        log.info("Lock domain for project has started for domain: {}", domain);
        lockService.lock(domain, LockSourceType.PROJECT_DOMAIN);
        log.info("Lock domain for project has completed for domain: {}", domain);
    }

    public void unlockDomain(String domain) {
        log.info("Unlock domain for project has started for domain: {}", domain);
        lockService.unlock(domain, LockSourceType.PROJECT_DOMAIN);
        log.info("Unlock domain for project has completed for domain: {}", domain);
    }
}
