package co.jinear.core.controller.auth;

import co.jinear.core.manager.auth.LoginManager;
import co.jinear.core.manager.auth.LogoutManager;
import co.jinear.core.model.request.auth.AuthCompleteRequest;
import co.jinear.core.model.request.auth.AuthInitializeRequest;
import co.jinear.core.model.request.auth.LoginWithPasswordRequest;
import co.jinear.core.model.response.auth.AuthInitializeResponse;
import co.jinear.core.model.response.auth.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/auth")
public class AuthController {

    private final LoginManager loginManager;
    private final LogoutManager logoutManager;

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletResponse response) {
        logoutManager.logout(response);
    }

    @PostMapping("/otp/email/initialize")
    @ResponseStatus(HttpStatus.OK)
    public AuthInitializeResponse loginWithEmailOtpInitialize(@Valid @RequestBody AuthInitializeRequest loginRequest) {
        return loginManager.loginWithEmailOtpInitialize(loginRequest);
    }

    @PostMapping("/otp/email/complete")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse emailOtpLoginComplete(@Valid @RequestBody AuthCompleteRequest authCompleteRequest, HttpServletResponse response) {
        return loginManager.emailOtpLoginComplete(authCompleteRequest, response);
    }

    @PostMapping("/password/email")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse loginWithPassword(@Valid @RequestBody LoginWithPasswordRequest loginWithPasswordRequest, HttpServletResponse response) {
        return loginManager.loginWithPassword(loginWithPasswordRequest, response);
    }
}
