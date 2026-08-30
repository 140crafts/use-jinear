package co.jinear.core.controller.mcp;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.mcp.McpAuthorizationManager;
import co.jinear.core.model.request.mcp.McpConsentRequest;
import co.jinear.core.model.response.mcp.McpConsentInfoResponse;
import co.jinear.core.model.response.mcp.McpConsentResponse;
import co.jinear.core.model.vo.mcp.McpAuthorizeRequestVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(value = "v1/oauth")
@RequiredArgsConstructor
public class McpOauthAuthorizeController {

    private final McpAuthorizationManager mcpAuthorizationManager;

    /**
     * Starts the flow. Always answers with a redirect: to the consent screen when the
     * request is usable, or back to the client with an OAuth error when it is not.
     * <p>
     * A bad client_id or an unregistered redirect_uri is the exception. There is no
     * address we trust to send the user to, so the error is rendered here instead of
     * being bounced to an attacker controlled URL.
     */
    @GetMapping("/authorize")
    public ResponseEntity<String> authorize(@RequestParam(value = "response_type", required = false) String responseType,
                                            @RequestParam(value = "client_id", required = false) String clientId,
                                            @RequestParam(value = "redirect_uri", required = false) String redirectUri,
                                            @RequestParam(value = "scope", required = false) String scope,
                                            @RequestParam(value = "state", required = false) String state,
                                            @RequestParam(value = "code_challenge", required = false) String codeChallenge,
                                            @RequestParam(value = "code_challenge_method", required = false) String codeChallengeMethod,
                                            @RequestParam(value = "resource", required = false) String resource) {
        McpAuthorizeRequestVo vo = McpAuthorizeRequestVo.builder()
                .responseType(responseType)
                .clientId(clientId)
                .redirectUri(redirectUri)
                .scope(scope)
                .state(state)
                .codeChallenge(codeChallenge)
                .codeChallengeMethod(codeChallengeMethod)
                .resource(resource)
                .build();
        try {
            String location = mcpAuthorizationManager.authorize(vo);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(location))
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .build();
        } catch (BusinessException exception) {
            log.warn("[MCP] Authorization request refused before redirect. reason: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorPage(exception.getMessage()));
        }
    }

    @GetMapping("/authorize/info/{requestId}")
    public McpConsentInfoResponse retrieveConsentInfo(@PathVariable String requestId) {
        return mcpAuthorizationManager.retrieveConsentInfo(requestId);
    }

    @PostMapping("/authorize/consent")
    public McpConsentResponse submitConsent(@Valid @RequestBody McpConsentRequest mcpConsentRequest) {
        return mcpAuthorizationManager.submitConsent(mcpConsentRequest);
    }

    /**
     * A deliberately plain page. It is only reached when a client is misconfigured, and
     * it must not leak anything about the account or link anywhere the caller supplied.
     */
    private String errorPage(String messageKey) {
        String detail = switch (messageKey) {
            case "mcp.error.oauth.invalid-client" -> "The application requesting access could not be verified.";
            case "mcp.error.oauth.invalid-redirect-uri" -> "The application sent a return address it has not registered.";
            case "mcp.error.disabled" -> "The MCP server is turned off on this instance.";
            default -> "This connection request could not be started.";
        };
        return """
                <!doctype html>
                <html lang="en"><head><meta charset="utf-8"><title>Connection request refused</title></head>
                <body style="font-family:system-ui,sans-serif;max-width:38rem;margin:6rem auto;padding:0 1.5rem;line-height:1.6">
                <h1 style="font-size:1.25rem">Connection request refused</h1>
                <p>%s</p>
                <p style="color:#666">Nothing was shared. You can close this window and try connecting again from your client.</p>
                </body></html>
                """.formatted(detail);
    }
}
