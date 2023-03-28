package co.jinear.core.service.account;

import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountLockService {
    private final LockService lockService;

    public void lockAccountForPasswordReset(String accountId) {
        log.info("Lock account for password reset has started for accountId: {}", accountId);
        lockService.lock(accountId, LockSourceType.ACCOUNT_PASSWORD_RESET);
        log.info("Lock account for password reset has completed for accountId: {}", accountId);
    }

    public void unlockAccountForPasswordReset(String accountId) {
        log.info("Unlock account for password reset has started for accountId: {}", accountId);
        lockService.unlock(accountId, LockSourceType.ACCOUNT_PASSWORD_RESET);
        log.info("Unlock account for password reset has completed for accountId: {}", accountId);
    }
}
