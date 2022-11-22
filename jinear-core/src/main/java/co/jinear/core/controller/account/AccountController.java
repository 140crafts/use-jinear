package co.jinear.core.controller.account;

import co.jinear.core.manager.account.AccountManager;
import co.jinear.core.manager.role.AccountRoleManager;
import co.jinear.core.model.request.account.ConfirmEmailRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.account.AccountRetrieveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/account")
public class AccountController {

    private final AccountManager accountManager;
    private final AccountRoleManager accountRoleManager;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AccountRetrieveResponse retrieveCurrentAccount() {
        accountRoleManager.refreshCurrentAccountRoles();
        return accountManager.retrieveCurrentAccount();
    }

    @PostMapping("/confirm-email")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse confirmEmail(@RequestBody ConfirmEmailRequest confirmEmailRequest) {
        return accountManager.confirmEmail(confirmEmailRequest);
    }
}
