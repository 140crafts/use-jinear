package co.jinear.core.controller.account;

import co.jinear.core.manager.account.AccountPasswordManager;
import co.jinear.core.model.request.account.CompleteResetPasswordRequest;
import co.jinear.core.model.request.account.InitializeResetPasswordRequest;
import co.jinear.core.model.request.account.UpdatePasswordRequest;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/account/password")
public class AccountPasswordController {

    private final AccountPasswordManager accountPasswordManager;

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return accountPasswordManager.updateAccountPassword(updatePasswordRequest);
    }

    @PostMapping("/reset/initialize")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse initializeResetPassword(@Valid @RequestBody InitializeResetPasswordRequest initializeResetPasswordRequest) {
        return accountPasswordManager.initializeResetAccountPassword(initializeResetPasswordRequest);
    }

    @PostMapping("/reset/complete")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse completeResetPassword(@RequestBody CompleteResetPasswordRequest completeResetPasswordRequest) {
        return accountPasswordManager.validateAndResetPassword(completeResetPasswordRequest);
    }
}
