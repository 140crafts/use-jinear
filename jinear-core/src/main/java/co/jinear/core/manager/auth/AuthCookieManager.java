package co.jinear.core.manager.auth;

import co.jinear.core.system.JwtHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import static co.jinear.core.system.JwtHelper.JWT_COOKIE;
import static co.jinear.core.system.JwtHelper.JWT_TOKEN_VALIDITY;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCookieManager {

    private final JwtHelper jwtHelper;

    public void addAuthCookie(String token, HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie
                .from(JWT_COOKIE, token)
                .secure(jwtHelper.isSecure())
                .httpOnly(true)
                .path("/")
                .domain(jwtHelper.getDomain())
                .maxAge(JWT_TOKEN_VALIDITY * 24 * 60 * 60)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public void invalidateAuthCookie(HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie
                .from(JWT_COOKIE, null)
                .secure(jwtHelper.isSecure())
                .httpOnly(true)
                .path("/")
                .domain(jwtHelper.getDomain())
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}
