package co.jinear.core.manager.account;

import co.jinear.core.model.dto.account.AccountDeleteEligibilityDto;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.account.AccountDeletionEligibilityResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountDeleteService;
import co.jinear.core.service.account.AccountLogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDeleteManager {

    private final SessionInfoService sessionInfoService;
    private final AccountDeleteService accountDeleteService;
    private final AccountLogoutService accountLogoutService;

    public BaseResponse sendAccountDeleteEmail() {
        String accountId = sessionInfoService.currentAccountId();
        accountDeleteService.validateEligibility(accountId);
        log.info("Send account delete email has started. accountId: {}", accountId);
        accountDeleteService.sendAccountDeleteConfirmationMail(accountId);
        return new BaseResponse();
    }

    public AccountDeletionEligibilityResponse checkEligibility() {
        String accountId = sessionInfoService.currentAccountId();
        log.info("Check eligibility has started. accountId: {}", accountId);
        AccountDeleteEligibilityDto accountDeleteEligibilityDto = accountDeleteService.retrieveEligibility(accountId);
        return mapResponse(accountDeleteEligibilityDto);
    }

    public BaseResponse confirmDelete(String token, HttpServletRequest request, HttpServletResponse response) {
        log.info("Confirm account delete has started.");
        String currentAccountId = sessionInfoService.currentAccountId();
        accountDeleteService.confirmDelete(token, currentAccountId);
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        accountLogoutService.logout(currentAccountId, currentAccountSessionId, request, response);
        return new BaseResponse();
    }

    public BaseResponse deleteWithoutConfirmation(HttpServletRequest request, HttpServletResponse response){
        log.info("Delete without confirmation has started.");
        String currentAccountId = sessionInfoService.currentAccountId();
        accountDeleteService.validateEligibilityAndAnonymize(currentAccountId);
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        accountLogoutService.logout(currentAccountId, currentAccountSessionId, request, response);
        return new BaseResponse();
    }

    private AccountDeletionEligibilityResponse mapResponse(AccountDeleteEligibilityDto accountDeleteEligibilityDto) {
        AccountDeletionEligibilityResponse accountDeletionEligibilityResponse = new AccountDeletionEligibilityResponse();
        accountDeletionEligibilityResponse.setAccountDeleteEligibilityDto(accountDeleteEligibilityDto);
        return accountDeletionEligibilityResponse;
    }
}
