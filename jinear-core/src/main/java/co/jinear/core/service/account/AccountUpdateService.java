package co.jinear.core.service.account;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountUpdateService {

    private final AccountRepository accountRepository;

    @Transactional
    public void updateEmailConfirmed(String accountId, boolean emailConfirmed) {
        log.info("Update email confirmed has started accountId: {}, emailConfirmed: {}", accountId, emailConfirmed);
        accountRepository.updateEmailConfirmed(accountId, emailConfirmed);
    }

    @Transactional
    public void updateAccountLocale(String accountId, LocaleType localeType) {
        log.info("Update account locale has started. accountId: {}, localeType: {}", accountId, localeType);
        accountRepository.updateLocaleType(accountId, localeType);
    }
}
