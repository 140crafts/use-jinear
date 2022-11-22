package co.jinear.core.manager.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutManager {

    private final AuthCookieManager authCookieManager;

    public void logout(HttpServletResponse response){
        authCookieManager.invalidateAuthCookie(response);
    }
}
