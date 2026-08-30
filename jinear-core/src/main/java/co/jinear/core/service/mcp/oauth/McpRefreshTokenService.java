package co.jinear.core.service.mcp.oauth;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.mcp.McpRefreshToken;
import co.jinear.core.repository.mcp.McpRefreshTokenRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.RandomHelper;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Rotating refresh tokens with reuse detection.
 * <p>
 * OAuth 2.1 requires rotation for public clients, and both DCR and CIMD register an
 * MCP client as public. Presenting a token that has already been rotated means a copy
 * leaked, so the whole connection is revoked instead of just refusing that one token.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpRefreshTokenService {

    private static final String SEPARATOR = ".";

    private final McpRefreshTokenRepository mcpRefreshTokenRepository;
    private final McpConnectionService mcpConnectionService;
    private final McpProperties mcpProperties;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PassiveService passiveService;

    public String issue(String connectionId) {
        McpRefreshToken token = new McpRefreshToken();
        token.setMcpConnectionId(connectionId);
        token.setExpiresAt(DateHelper.addDays(DateHelper.now(), mcpProperties.getRefreshTokenValidityDays()));
        String secret = RandomHelper.generateULID() + RandomHelper.generateULID();
        token.setHashedToken(bCryptPasswordEncoder.encode(secret));
        McpRefreshToken saved = mcpRefreshTokenRepository.save(token);
        return saved.getMcpRefreshTokenId() + SEPARATOR + secret;
    }

    public McpRefreshToken redeem(String presentedToken) {
        if (Objects.isNull(presentedToken) || !presentedToken.contains(SEPARATOR)) {
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }
        int separatorIndex = presentedToken.indexOf(SEPARATOR);
        String tokenId = presentedToken.substring(0, separatorIndex);
        String secret = presentedToken.substring(separatorIndex + 1);

        McpRefreshToken token = mcpRefreshTokenRepository
                .findByMcpRefreshTokenIdAndPassiveIdIsNull(tokenId)
                .orElseThrow(() -> new BusinessException("mcp.error.oauth.invalid-grant"));

        if (!bCryptPasswordEncoder.matches(secret, token.getHashedToken())) {
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }
        if (Objects.nonNull(token.getConsumedAt())) {
            log.warn("[MCP] Refresh token reuse detected, revoking connection. mcpConnectionId: {}",
                    token.getMcpConnectionId());
            revokeAllForConnection(token.getMcpConnectionId());
            mcpConnectionService.revoke(token.getMcpConnectionId());
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }
        if (token.getExpiresAt().before(DateHelper.now())) {
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }
        return token;
    }

    /** Marks the presented token spent and returns its successor, in one step. */
    public String rotate(McpRefreshToken current) {
        String replacement = issue(current.getMcpConnectionId());
        String replacementId = replacement.substring(0, replacement.indexOf(SEPARATOR));
        current.setConsumedAt(DateHelper.now());
        current.setRotatedTo(replacementId);
        mcpRefreshTokenRepository.save(current);
        return replacement;
    }

    public void revokeAllForConnection(String connectionId) {
        List<McpRefreshToken> tokens = mcpRefreshTokenRepository.findAllByMcpConnectionIdAndPassiveIdIsNull(connectionId);
        if (tokens.isEmpty()) {
            return;
        }
        String passiveId = passiveService.createSystemActionPassive();
        tokens.forEach(token -> token.setPassiveId(passiveId));
        mcpRefreshTokenRepository.saveAll(tokens);
    }
}
