package co.jinear.core.manager.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import static co.jinear.core.system.JwtHelper.JWT_TOKEN_VALIDITY;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCookieManager {

    private static final String JWT = "jwt";

    public void addAuthCookie(String token, HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie
                .from(JWT,  token)
//                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(JWT_TOKEN_VALIDITY * 24 * 60 * 60)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public void invalidateAuthCookie(HttpServletResponse response){
        ResponseCookie responseCookie = ResponseCookie
                .from(JWT,  null)
//                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}
