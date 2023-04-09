package co.jinear.core.manager.auth;

import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.notification.NotificationTargetOperationService;
import co.jinear.core.service.passive.PassiveService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutManager {

    private final AuthCookieManager authCookieManager;
    private final SessionInfoService sessionInfoService;
    private final NotificationTargetOperationService notificationTargetOperationService;
    private final PassiveService passiveService;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        log.info("Logout has started. currentAccountId: {}, currentAccountSessionId: {}", currentAccountId, currentAccountSessionId);
        detachNotificationTarget(currentAccountId, currentAccountSessionId);
        authCookieManager.invalidateAuthCookie(response);
        logout(request);
    }

    private void detachNotificationTarget(String currentAccountId, String currentAccountSessionId) {
        log.info("Detaching notification target. currentAccountId: {}, currentAccountSessionId: {}", currentAccountId, currentAccountSessionId);
        try {
            String passiveId = notificationTargetOperationService.removeNotificationTarget(currentAccountSessionId);
            passiveService.assignOwnership(passiveId, currentAccountId);
        } catch (Exception e) {
            log.error("Detach notification target failed.", e);
        }
    }

    private void logout(HttpServletRequest request) {
        try {
            request.logout();
        } catch (Exception e) {
            log.error("Logout failed.", e);
        }
    }
}
