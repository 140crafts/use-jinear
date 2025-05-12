package co.jinear.core.manager.account;

import co.jinear.core.converter.account.SetCommunicationPermissionsRequestConverter;
import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.request.account.SetCommunicationPermissionsRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.account.AccountCommunicationPermissionsResponse;
import co.jinear.core.model.vo.account.UpdateAccountCommunicationPermissionsVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountCommunicationPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCommunicationPermissionManager {

    private final SessionInfoService sessionInfoService;
    private final SetCommunicationPermissionsRequestConverter setCommunicationPermissionsRequestConverter;
    private final AccountCommunicationPermissionService accountCommunicationPermissionService;

    public AccountCommunicationPermissionsResponse retrievePermissions() {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve account communication permissions has started. currentAccountId: {}", currentAccountId);
        AccountCommunicationPermissionDto accountCommunicationPermissionDto = accountCommunicationPermissionService.retrieve(currentAccountId);
        return mapResponse(accountCommunicationPermissionDto);
    }

    public BaseResponse setPermissions(SetCommunicationPermissionsRequest communicationPermissionsRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Set account communication permissions has started. currentAccountId: {}", currentAccountId);
        UpdateAccountCommunicationPermissionsVo updateAccountCommunicationPermissionsVo = setCommunicationPermissionsRequestConverter.convert(currentAccountId, communicationPermissionsRequest);
        accountCommunicationPermissionService.update(updateAccountCommunicationPermissionsVo);
        return new BaseResponse();
    }

    private AccountCommunicationPermissionsResponse mapResponse(AccountCommunicationPermissionDto accountCommunicationPermissionDto) {
        AccountCommunicationPermissionsResponse accountCommunicationPermissionsResponse = new AccountCommunicationPermissionsResponse();
        accountCommunicationPermissionsResponse.setAccountCommunicationPermissionDto(accountCommunicationPermissionDto);
        return accountCommunicationPermissionsResponse;
    }
}
