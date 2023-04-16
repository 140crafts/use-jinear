package co.jinear.core.controller.account;

import co.jinear.core.manager.account.AccountCommunicationPermissionManager;
import co.jinear.core.model.request.account.SetCommunicationPermissionsRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.account.AccountCommunicationPermissionsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/account/communication-permission")
public class AccountCommunicationPermissionController {

    private final AccountCommunicationPermissionManager accountCommunicationPermissionManager;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AccountCommunicationPermissionsResponse retrievePermissions() {
        return accountCommunicationPermissionManager.retrievePermissions();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse setPermissions(@Valid @RequestBody SetCommunicationPermissionsRequest communicationPermissionsRequest) {
        return accountCommunicationPermissionManager.setPermissions(communicationPermissionsRequest);
    }
}
