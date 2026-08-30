package co.jinear.core.service.mcp.oauth;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.mcp.McpAuthorizationCode;
import co.jinear.core.model.entity.mcp.McpAuthorizationRequest;
import co.jinear.core.repository.mcp.McpAuthorizationCodeRepository;
import co.jinear.core.system.RandomHelper;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * Issues and redeems authorization codes.
 * <p>
 * The code handed to the client is "{rowId}.{secret}". Storing only a BCrypt hash of
 * the secret follows the same shape as the robot token: a database leak does not
 * yield usable codes, and the row id keeps the lookup indexable.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpAuthorizationCodeService {

    private static final String SEPARATOR = ".";

    private final McpAuthorizationCodeRepository mcpAuthorizationCodeRepository;
    private final McpProperties mcpProperties;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String issue(McpAuthorizationRequest request, String accountId, String connectionId) {
        McpAuthorizationCode code = new McpAuthorizationCode();
        code.setAccountId(accountId);
        code.setClientId(request.getClientId());
        code.setMcpConnectionId(connectionId);
        code.setRedirectUri(request.getRedirectUri());
        code.setScope(request.getScope());
        code.setCodeChallenge(request.getCodeChallenge());
        code.setCodeChallengeMethod(request.getCodeChallengeMethod());
        code.setResource(request.getResource());
        code.setExpiresAt(DateHelper.addSeconds(DateHelper.now(), mcpProperties.getAuthorizationCodeValiditySeconds()));

        String secret = RandomHelper.generateULID() + RandomHelper.generateULID();
        code.setHashedCode(bCryptPasswordEncoder.encode(secret));
        McpAuthorizationCode saved = mcpAuthorizationCodeRepository.save(code);
        return saved.getMcpAuthorizationCodeId() + SEPARATOR + secret;
    }

    /**
     * Consumes a code exactly once. A replay marks the row consumed already, which is
     * treated as invalid_grant rather than silently reissuing a token.
     */
    public McpAuthorizationCode redeem(String presentedCode) {
        if (Objects.isNull(presentedCode) || !presentedCode.contains(SEPARATOR)) {
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }
        int separatorIndex = presentedCode.indexOf(SEPARATOR);
        String codeId = presentedCode.substring(0, separatorIndex);
        String secret = presentedCode.substring(separatorIndex + 1);

        McpAuthorizationCode code = mcpAuthorizationCodeRepository
                .findByMcpAuthorizationCodeIdAndPassiveIdIsNull(codeId)
                .orElseThrow(() -> new BusinessException("mcp.error.oauth.invalid-grant"));

        if (Objects.nonNull(code.getConsumedAt())) {
            log.warn("[MCP] Authorization code replayed. codeId: {}", codeId);
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }
        if (code.getExpiresAt().before(DateHelper.now())) {
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }
        if (!bCryptPasswordEncoder.matches(secret, code.getHashedCode())) {
            throw new BusinessException("mcp.error.oauth.invalid-grant");
        }

        code.setConsumedAt(DateHelper.now());
        mcpAuthorizationCodeRepository.save(code);
        return code;
    }

    public int purgeExpiredBefore(Date before) {
        return mcpAuthorizationCodeRepository.deleteAllExpiredBefore(before);
    }
}
