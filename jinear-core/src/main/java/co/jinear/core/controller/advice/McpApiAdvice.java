package co.jinear.core.controller.advice;

import co.jinear.core.exception.mcp.McpAuthChallengeException;
import co.jinear.core.exception.mcp.McpDisabledException;
import co.jinear.core.model.response.mcp.McpProtocolErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Renders MCP auth challenges. The MCP endpoint answers RFC 6750, not the Jinear
 * {@code BaseResponse} envelope, so it gets its own advice rather than sharing
 * {@code GeneralApiAdvice}.
 */
@Slf4j
@RestControllerAdvice
public class McpApiAdvice {

    @ExceptionHandler(value = McpDisabledException.class)
    protected ResponseEntity<Void> handleDisabled(McpDisabledException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = McpAuthChallengeException.class)
    protected ResponseEntity<McpProtocolErrorResponse> handleAuthChallenge(McpAuthChallengeException exception) {
        HttpStatus status = exception.isForbidden() ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED;
        log.info("[MCP] Answering {} with {}.", status.value(), exception.getErrorCode());
        return ResponseEntity.status(status)
                .header(HttpHeaders.WWW_AUTHENTICATE, exception.getChallenge())
                .body(new McpProtocolErrorResponse(exception.getErrorCode(), exception.getMessage()));
    }
}
