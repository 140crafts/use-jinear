package co.jinear.core.service.account;

import co.jinear.core.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
