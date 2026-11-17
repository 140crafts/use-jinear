package co.jinear.core.manager.account;

import co.jinear.core.converter.workspace.AccountsWorkspacePerspectiveDtoConverter;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.workspace.AccountsWorkspacePerspectiveDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.request.account.ConfirmEmailRequest;
import co.jinear.core.model.request.account.ResendConfirmEmailRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.account.AccountRetrieveResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountMailConfirmationService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountManager {


    private final AccountRetrieveService accountRetrieveService;
    private final AccountMailConfirmationService accountMailConfirmationService;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final AccountsWorkspacePerspectiveDtoConverter accountsWorkspacePerspectiveDtoConverter;
    private final MediaRetrieveService mediaRetrieveService;

    public AccountRetrieveResponse retrieveCurrentAccount() {
        log.info("Retrieve current account has started.");
        String accountId = sessionInfoService.currentAccountId();
        String currentSessionId = sessionInfoService.currentAccountSessionId();
        AccountDto accountDto = accountRetrieveService.retrieveWithBasicInfo(accountId);
        setWorkspaces(accountId, accountDto);
        return mapAccountRetrieveResponse(accountDto, Optional.of(currentSessionId));
    }

    public AccountRetrieveResponse retrieveAccount(String accountId) {
        log.info("Retrieve account has started. accountId: {}", accountId);
        AccountDto accountDto = accountRetrieveService.retrieveWithBasicInfo(accountId);
        setWorkspaces(accountId, accountDto);
        return mapAccountRetrieveResponse(accountDto, Optional.empty());
    }

    public BaseResponse confirmEmail(ConfirmEmailRequest confirmEmailRequest) {
        accountMailConfirmationService.confirmEmail(confirmEmailRequest.getEmail(), confirmEmailRequest.getCode());
        return new BaseResponse();
    }

    public BaseResponse resendConfirmEmail(ResendConfirmEmailRequest resendConfirmEmailRequest) {
        log.info("Resend confirm email has started. email: {}", resendConfirmEmailRequest.getEmail());
        accountMailConfirmationService.resendConfirmEmailMail(resendConfirmEmailRequest.getEmail(), resendConfirmEmailRequest.getLocale());
        return new BaseResponse();
    }

    private AccountRetrieveResponse mapAccountRetrieveResponse(AccountDto accountDto, Optional<String> currentSessionIdOptional) {
        AccountRetrieveResponse accountRetrieveResponse = new AccountRetrieveResponse();
        accountRetrieveResponse.setAccountDto(accountDto);
        currentSessionIdOptional.ifPresent(accountRetrieveResponse::setSessionId);
        return accountRetrieveResponse;
    }

    private void setWorkspaces(String accountId, AccountDto accountDto) {
        List<DetailedWorkspaceMemberDto> workspaces = workspaceRetrieveService.retrieveAccountWorkspaces(accountId);
        List<AccountsWorkspacePerspectiveDto> accountsWorkspacePerspectiveDtos = workspaces.stream()
                .map(accountsWorkspacePerspectiveDtoConverter::convert)
                .map(this::setProfilePicture)
                .toList();
        accountDto.setWorkspaces(accountsWorkspacePerspectiveDtos);
    }

    private AccountsWorkspacePerspectiveDto setProfilePicture(AccountsWorkspacePerspectiveDto workspaceDto) {
        mediaRetrieveService.retrieveProfilePictureOptional(workspaceDto.getWorkspaceId())
                .ifPresent(workspaceDto::setProfilePicture);
        return workspaceDto;
    }
}
