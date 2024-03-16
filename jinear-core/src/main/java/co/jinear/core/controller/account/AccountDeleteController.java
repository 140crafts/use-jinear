package co.jinear.core.controller.account;

import co.jinear.core.manager.account.AccountDeleteManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.account.AccountDeletionEligibilityResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/account/delete")
public class AccountDeleteController {

    private final AccountDeleteManager accountDeleteManager;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse sendAccountDeleteEmail() {
        return accountDeleteManager.sendAccountDeleteEmail();
    }

    @GetMapping("/eligibility")
    @ResponseStatus(HttpStatus.OK)
    public AccountDeletionEligibilityResponse checkEligibility() {
        return accountDeleteManager.checkEligibility();
    }

    @DeleteMapping("/confirm/{token}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse confirmDelete(@PathVariable String token, HttpServletRequest request, HttpServletResponse response) {
        return accountDeleteManager.confirmDelete(token, request, response);
    }
}
