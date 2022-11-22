package co.jinear.core.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Optional;

@Slf4j
public class AcceptLanguageHeaderInterceptor implements HandlerInterceptor {

    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String acceptLanguage = request.getHeader(ACCEPT_LANGUAGE_HEADER);
            Optional.ofNullable(acceptLanguage)
                    .map(Locale::new)
                    .ifPresent(LocaleContextHolder::setLocale);
        } catch (Exception e) {
            log.error("Caught an exception while executing handler method", e);
        }
        return true;
    }
}