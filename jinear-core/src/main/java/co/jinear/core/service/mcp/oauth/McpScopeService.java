package co.jinear.core.service.mcp.oauth;

import co.jinear.core.model.enumtype.mcp.McpScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class McpScopeService {

    /**
     * Parses a space delimited scope string, dropping anything this server does not
     * define. Silently narrowing beats failing the whole authorization, because a
     * generic client may ask for scopes it read from another server's metadata.
     */
    public Set<String> parse(String scope) {
        if (Objects.isNull(scope) || scope.isBlank()) {
            return new LinkedHashSet<>();
        }
        Set<String> parsed = new LinkedHashSet<>();
        Arrays.stream(scope.trim().split("\\s+"))
                .filter(candidate -> McpScope.ofValue(candidate).isPresent())
                .forEach(parsed::add);
        return parsed;
    }

    /** Every scope, used when a client asks for none at all. */
    public Set<String> defaultScopes() {
        return new LinkedHashSet<>(McpScope.allValues());
    }

    public String format(Set<String> scopes) {
        return String.join(" ", scopes);
    }

    public boolean grants(Set<String> granted, Set<String> required) {
        return granted.containsAll(required);
    }
}
