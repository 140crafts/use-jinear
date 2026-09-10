package co.jinear.core.validator.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.mcp.McpDisabledException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class McpEnabledValidator {

    private final McpProperties mcpProperties;

    public void validateMcpIsEnabled() {
        if (!Boolean.TRUE.equals(mcpProperties.getEnabled())) {
            throw new McpDisabledException();
        }
    }
}
