package co.jinear.core.service.mcp.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

/**
 * PKCE, S256 only.
 * <p>
 * OAuth 2.1 requires S256 where the client can do it, and every MCP client can, so
 * the "plain" method is not accepted at all. Our authorization server metadata
 * advertises only S256 in code_challenge_methods_supported to match.
 */
@Slf4j
@Component
public class McpPkceValidator {

    public static final String METHOD_S256 = "S256";

    public boolean isSupportedMethod(String method) {
        return METHOD_S256.equals(method);
    }

    public boolean verify(String codeVerifier, String codeChallenge) {
        if (Objects.isNull(codeVerifier) || Objects.isNull(codeChallenge)) {
            return false;
        }
        if (codeVerifier.length() < 43 || codeVerifier.length() > 128) {
            return false;
        }
        return MessageDigest.isEqual(
                derive(codeVerifier).getBytes(StandardCharsets.US_ASCII),
                codeChallenge.getBytes(StandardCharsets.US_ASCII));
    }

    public String derive(String codeVerifier) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}
