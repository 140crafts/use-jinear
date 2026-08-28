package co.jinear.core.service.account;

import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementAccountLockService {

    private final LockService lockService;

    public void lockForManagementAccountRoleSync() {
        log.info("Lock for management account role sync has started.");
        lockService.lock("", LockSourceType.MANAGEMENT_ADMIN_ACCOUNT_SYNC);
        log.info("Lock for management account role sync has completed.");
    }

    public void unlockForManagementAccountRoleSync() {
        log.info("Unlock for management account role sync has started.");
        lockService.unlock("", LockSourceType.MANAGEMENT_ADMIN_ACCOUNT_SYNC);
        log.info("Unlock for management account role sync has completed.");
    }
}
