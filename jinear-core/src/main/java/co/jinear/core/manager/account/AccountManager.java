package co.jinear.core.manager.account;

import co.jinear.core.converter.workspace.AccountsWorkspacePerspectiveDtoConverter;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.dto.workspace.AccountsWorkspacePerspectiveDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.enumtype.token.TokenType;
import co.jinear.core.model.request.account.ConfirmEmailRequest;
import co.jinear.core.model.request.account.ResendConfirmEmailRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.account.AccountRetrieveResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountMailConfirmationService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.token.TokenService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountManager {

    private static final String ANONYMOUS_USER = "anonymousUser";

    private final AccountRetrieveService accountRetrieveService;
    private final AccountMailConfirmationService accountMailConfirmationService;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final TokenService tokenService;
    private final AccountsWorkspacePerspectiveDtoConverter accountsWorkspacePerspectiveDtoConverter;

    public AccountRetrieveResponse retrieveCurrentAccount() {
        log.info("Retrieve current account has started.");
        String accountId = sessionInfoService.currentAccountId();
        AccountDto accountDto = accountRetrieveService.retrieveWithBasicInfo(accountId);
        setWorkspaces(accountId, accountDto);
        return mapAccountRetrieveResponse(accountDto);
    }

    public BaseResponse confirmEmail(ConfirmEmailRequest confirmEmailRequest) {
        accountMailConfirmationService.confirmEmail(confirmEmailRequest.getUniqueToken());
        return new BaseResponse();
    }

    public BaseResponse resendConfirmEmail(ResendConfirmEmailRequest resendConfirmEmailRequest) {
        String accountId = sessionInfoService.currentAccountIdInclAnonymous();
        log.info("Resend confirm email has started from accountId: {}", accountId);
        decideAndResendEmail(resendConfirmEmailRequest, accountId);
        return new BaseResponse();
    }

    private AccountRetrieveResponse mapAccountRetrieveResponse(AccountDto accountDto) {
        AccountRetrieveResponse accountRetrieveResponse = new AccountRetrieveResponse();
        accountRetrieveResponse.setAccountDto(accountDto);
        return accountRetrieveResponse;
    }

    private void setWorkspaces(String accountId, AccountDto accountDto) {
        List<DetailedWorkspaceMemberDto> workspaces = workspaceRetrieveService.retrieveAccountWorkspaces(accountId);
        List<AccountsWorkspacePerspectiveDto> accountsWorkspacePerspectiveDtos = workspaces.stream()
                .map(accountsWorkspacePerspectiveDtoConverter::convert)
                .toList();
        accountDto.setWorkspaces(accountsWorkspacePerspectiveDtos);
    }

    private void decideAndResendEmail(ResendConfirmEmailRequest resendConfirmEmailRequest, String accountId) {
        if (ANONYMOUS_USER.equalsIgnoreCase(accountId)) {
            TokenDto tokenDto = tokenService.retrieveToken(resendConfirmEmailRequest.getToken(), TokenType.CONFIRM_EMAIL);
            accountMailConfirmationService.sendConfirmEmailMail(tokenDto.getRelatedObject(), resendConfirmEmailRequest.getLocale());
        } else {
            accountMailConfirmationService.sendConfirmEmailMail(accountId, resendConfirmEmailRequest.getLocale());
        }
    }
}
