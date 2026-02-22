package co.jinear.core.controller.auth;

import co.jinear.core.manager.auth.LoginManager;
import co.jinear.core.manager.auth.LogoutManager;
import co.jinear.core.model.request.auth.*;
import co.jinear.core.model.response.auth.AuthInitializeResponse;
import co.jinear.core.model.response.auth.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/auth")
public class AuthController {

    private final LoginManager loginManager;
    private final LogoutManager logoutManager;

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        logoutManager.logout(request, response);
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

    @PostMapping("/sign-in-with-apple")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse loginWithApple(@Valid @RequestBody LoginWithAppleRequest loginWithAppleRequest, HttpServletResponse response) {
        return loginManager.loginWithApple(loginWithAppleRequest, response);
    }

    @PostMapping("/single-use-token")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse loginWithSingleUseToken(@Valid @RequestBody SingleUseTokenLoginRequest singleUseTokenLoginRequest, HttpServletResponse response) {
        return loginManager.loginWithSingleUseToken(singleUseTokenLoginRequest, response);
    }
}
