package co.jinear.core.manager.account;

import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.request.account.ConfirmEmailRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.account.AccountRetrieveResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountMailConfirmationService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.team.TeamRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountManager {

    private final AccountRetrieveService accountRetrieveService;
    private final AccountMailConfirmationService accountMailConfirmationService;
    private final TeamRetrieveService teamRetrieveService;
    private final SessionInfoService sessionInfoService;

    public AccountRetrieveResponse retrieveCurrentAccount() {
        log.info("Retrieve current account has started.");
        String accountId = sessionInfoService.currentAccountId();
        AccountDto accountDto = accountRetrieveService.retrieveWithBasicInfo(accountId);
        setTeams(accountId, accountDto);
        return mapAccountRetrieveResponse(accountDto);
    }

    public BaseResponse confirmEmail(ConfirmEmailRequest confirmEmailRequest) {
        accountMailConfirmationService.confirmEmail(confirmEmailRequest.getUniqueToken());
        return BaseResponse.builder().build();
    }

    private AccountRetrieveResponse mapAccountRetrieveResponse(AccountDto accountDto) {
        AccountRetrieveResponse accountRetrieveResponse = new AccountRetrieveResponse();
        accountRetrieveResponse.setAccountDto(accountDto);
        return accountRetrieveResponse;
    }

    private void setTeams(String accountId, AccountDto accountDto) {
        List<TeamDto> teams = teamRetrieveService.retrieveAccountTeams(accountId);
        accountDto.setTeams(teams);
    }
}
