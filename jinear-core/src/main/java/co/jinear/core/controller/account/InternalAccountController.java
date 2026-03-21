package co.jinear.core.controller.account;

import co.jinear.core.manager.account.AccountManager;
import co.jinear.core.model.response.account.AccountRetrieveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/internal/account")
public class InternalAccountController {

    private final AccountManager accountManager;

    @GetMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountRetrieveResponse retrieve(@PathVariable String accountId) {
        return accountManager.retrieveAccount(accountId);
    }
}
