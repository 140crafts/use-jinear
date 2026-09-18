package co.jinear.core.validator.oauth;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

/**
 * Checks the RFC 8707 resource indicator against the resource this instance serves. Both the
 * authorize and the token endpoint need the same rule, so it lives in one place.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OauthResourceIndicatorValidator {

    private final McpProperties mcpProperties;

    public void validateResourceMatches(String resource, String errorMessageKey) {
        if (Objects.isNull(resource) || resource.isBlank()) {
            return;
        }
        if (!normalize(resource).equals(normalize(mcpProperties.getResourceUrl()))) {
            log.warn("[OAUTH] Token requested for another resource: {}", resource);
            throw new BusinessException(errorMessageKey);
        }
    }

    private String normalize(String uri) {
        String trimmed = uri.trim();
        if (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed.toLowerCase(Locale.ROOT);
    }
}
