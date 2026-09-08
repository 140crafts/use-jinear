package co.jinear.core.controller.advice;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.response.oauth.OauthErrorResponse;
import co.jinear.core.service.oauth.provider.OauthErrorMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Renders OAuth endpoint failures as the RFC 6749 error body, rather than the Jinear
 * {@code BaseResponse} envelope {@code GeneralApiAdvice} produces. Scoped to the OAuth
 * provider controllers so no other endpoint changes shape.
 */
@Slf4j
@RestControllerAdvice(basePackages = "co.jinear.core.controller.oauth.provider")
@RequiredArgsConstructor
public class OauthApiAdvice {

    private final OauthErrorMapper oauthErrorMapper;

    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<OauthErrorResponse> handleBusinessException(BusinessException exception) {
        String errorCode = oauthErrorMapper.errorCodeFor(exception.getMessage());
        HttpStatus status = oauthErrorMapper.statusFor(errorCode);
        log.warn("[OAUTH] OAuth endpoint returning {} {}", status.value(), errorCode);
        return ResponseEntity.status(status)
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .body(oauthErrorMapper.body(errorCode, null));
    }
}
