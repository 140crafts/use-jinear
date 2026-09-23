package co.jinear.core.controller.advice;

import co.jinear.core.config.locale.MessageSourceLocalizer;
import co.jinear.core.controller.oauth.provider.OauthAuthorizeRedirectController;
import co.jinear.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Renders a refused authorization request as a page, because a person's browser is what
 * reaches this endpoint. The wording comes from the message bundle keyed by the same
 * {@code oauth.error.*} keys the exception carries, so it is translated like everything else.
 */
@Slf4j
@RestControllerAdvice(assignableTypes = OauthAuthorizeRedirectController.class)
@RequiredArgsConstructor
public class OauthAuthorizeApiAdvice {

    private static final String PAGE_TEMPLATE = """
            <!doctype html>
            <html lang="en"><head><meta charset="utf-8"><title>%s</title></head>
            <body style="font-family:system-ui,sans-serif;max-width:38rem;margin:6rem auto;padding:0 1.5rem;line-height:1.6">
            <h1 style="font-size:1.25rem">%s</h1>
            <p>%s</p>
            <p style="color:#666">%s</p>
            </body></html>
            """;

    private final MessageSourceLocalizer messageSourceLocalizer;
    private final ApiAdviceHelper apiAdviceHelper;

    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<String> handleRefusedAuthorization(BusinessException exception) {
        log.warn("[OAUTH] Authorization request refused before redirect. reason: {}", exception.getMessage());
        String title = message("oauth.authorize.refused-title");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.TEXT_HTML)
                .body(PAGE_TEMPLATE.formatted(
                        title,
                        title,
                        message(exception.getMessage()),
                        message("oauth.authorize.refused-footer")));
    }

    private String message(String key) {
        return apiAdviceHelper.getErrorMessage(messageSourceLocalizer.getLocaleMessage(key));
    }
}
