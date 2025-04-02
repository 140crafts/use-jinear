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
        long maxAgeSeconds = JWT_TOKEN_VALIDITY * 24 * 60 * 60;
        ResponseCookie responseCookie = generateResponseCookie(token, maxAgeSeconds);
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public void invalidateAuthCookie(HttpServletResponse response) {
        ResponseCookie responseCookie = generateResponseCookie(null, 0);
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    private ResponseCookie generateResponseCookie(String token, long maxAgeSeconds) {
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = ResponseCookie
                .from(JWT_COOKIE, token)
                .secure(jwtHelper.isSecure())
                .httpOnly(true)
                .path("/")
                .maxAge(maxAgeSeconds);
        if (Boolean.TRUE.equals(jwtHelper.getIncludeDomain())) {
            responseCookieBuilder.domain(jwtHelper.getDomain());
        }
        if (Boolean.TRUE.equals(jwtHelper.getIncludeSameSite())) {
            responseCookieBuilder.sameSite(jwtHelper.getSameSite());
        }

        ResponseCookie responseCookie = responseCookieBuilder.build();
        return responseCookie;
    }
}
