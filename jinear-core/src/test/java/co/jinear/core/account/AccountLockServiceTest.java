package co.jinear.core.account;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.lock.LockedException;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.service.account.AccountLockService;
import co.jinear.core.service.lock.LockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountLockServiceTest {

    private static final String ACCOUNT_ID = "account-1";

    private final LockService lockService = Mockito.mock(LockService.class);
    private final AccountLockService accountLockService = new AccountLockService(lockService);

    @Test
    void firstConfirmEmailMailStartsCooldown() {
        assertDoesNotThrow(() -> accountLockService.startConfirmEmailMailCooldown(ACCOUNT_ID));

        Mockito.verify(lockService).lock(ACCOUNT_ID, LockSourceType.ACCOUNT_CONFIRM_EMAIL_MAIL);
    }

    @Test
    void confirmEmailMailInCooldownIsRejected() {
        Mockito.doThrow(new LockedException(LockSourceType.ACCOUNT_CONFIRM_EMAIL_MAIL, ACCOUNT_ID))
                .when(lockService).lock(ACCOUNT_ID, LockSourceType.ACCOUNT_CONFIRM_EMAIL_MAIL);

        BusinessException exception = assertThrows(BusinessException.class, () -> accountLockService.startConfirmEmailMailCooldown(ACCOUNT_ID));

        assertEquals("account.confirm-email-mail-cooldown", exception.getMessage());
    }
}
