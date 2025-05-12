package co.jinear.core.controller.account;

import co.jinear.core.manager.account.AccountRegisterManager;
import co.jinear.core.model.request.account.register.RegisterViaMailRequest;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/account/register")
public class AccountRegisterController {

    private final AccountRegisterManager accountRegisterManager;

    @PostMapping("/email")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse registerViaMail(@RequestBody RegisterViaMailRequest registerViaMailRequest) {
        return accountRegisterManager.registerViaMail(registerViaMailRequest);
    }
}
