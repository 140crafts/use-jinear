package co.jinear.core.controller.oauth.provider;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.oauth.provider.OauthAuthorizationManager;
import co.jinear.core.model.request.oauth.OauthConsentRequest;
import co.jinear.core.model.response.oauth.OauthConsentInfoResponse;
import co.jinear.core.model.response.oauth.OauthConsentResponse;
import co.jinear.core.model.vo.oauth.OauthAuthorizeRequestVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "v1/oauth")
@RequiredArgsConstructor
public class OauthAuthorizeController {

    private final OauthAuthorizationManager oauthAuthorizationManager;

    /**
     * Starts the flow. Always answers with a redirect: to the consent screen when the
     * request is usable, or back to the client with an OAuth error when it is not.
     * <p>
     * A bad client_id or an unregistered redirect_uri is the exception. There is no
     * address we trust to send the user to, so the error is rendered here instead of
     * being bounced to an attacker controlled URL.
     */
    @GetMapping("/authorize")
    public ResponseEntity<String> authorize(@RequestParam Map<String, String> params) {
        try {
            String location = oauthAuthorizationManager.authorize(OauthAuthorizeRequestVo.fromParams(params));
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(location))
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .build();
        } catch (BusinessException exception) {
            log.warn("[OAUTH] Authorization request refused before redirect. reason: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorPage(exception.getMessage()));
        }
    }

    @GetMapping("/authorize/info/{requestId}")
    public OauthConsentInfoResponse retrieveConsentInfo(@PathVariable String requestId) {
        return oauthAuthorizationManager.retrieveConsentInfo(requestId);
    }

    @PostMapping("/authorize/consent")
    public OauthConsentResponse submitConsent(@Valid @RequestBody OauthConsentRequest oauthConsentRequest) {
        return oauthAuthorizationManager.submitConsent(oauthConsentRequest);
    }

    /**
     * A deliberately plain page. It is only reached when a client is misconfigured, and
     * it must not leak anything about the account or link anywhere the caller supplied.
     */
    private String errorPage(String messageKey) {
        String detail = switch (messageKey) {
            case "oauth.error.invalid-client" -> "The application requesting access could not be verified.";
            case "oauth.error.invalid-redirect-uri" -> "The application sent a return address it has not registered.";
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
