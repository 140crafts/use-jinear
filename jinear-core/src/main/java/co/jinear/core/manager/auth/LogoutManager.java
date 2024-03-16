package co.jinear.core.manager.auth;

import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountLogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutManager {

    private final SessionInfoService sessionInfoService;
    private final AccountLogoutService accountLogoutService;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        accountLogoutService.logout(currentAccountId, currentAccountSessionId, request, response);
    }
}
