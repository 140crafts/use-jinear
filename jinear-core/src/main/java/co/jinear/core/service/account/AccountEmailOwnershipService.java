package co.jinear.core.service.account;

import co.jinear.core.model.dto.account.AccountDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountEmailOwnershipService {

    private final AccountRetrieveService accountRetrieveService;
    private final AccountPasswordService accountPasswordService;
    private final AccountUpdateService accountUpdateService;

    @Transactional
    public void confirmEmailOwnership(String accountId) {
        log.info("Confirm email ownership has started. accountId: {}", accountId);
        AccountDto accountDto = accountRetrieveService.retrieve(accountId);
        if (isEmailConfirmed(accountDto)) {
            return;
        }
        accountPasswordService.replaceWithRandomPassword(accountId);
        accountUpdateService.updateEmailConfirmed(accountId, Boolean.TRUE);
        log.info("Email ownership confirmed and unconfirmed password replaced. accountId: {}", accountId);
    }

    private boolean isEmailConfirmed(AccountDto accountDto) {
        return Boolean.TRUE.equals(accountDto.getEmailConfirmed());
    }
}
