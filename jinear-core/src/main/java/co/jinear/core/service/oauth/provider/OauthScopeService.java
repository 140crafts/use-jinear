package co.jinear.core.service.oauth.provider;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class OauthScopeService {

    public Set<String> parse(String scope) {
        if (Objects.isNull(scope) || scope.isBlank()) {
            return new LinkedHashSet<>();
        }
        Set<String> parsed = new LinkedHashSet<>();
        Arrays.stream(scope.trim().split("\\s+"))
                .filter(candidate -> OauthScope.ofValue(candidate).isPresent())
                .forEach(parsed::add);
        return parsed;
    }

    public Set<String> defaultScopes() {
        return new LinkedHashSet<>(OauthScope.allValues());
    }

    public String format(Set<String> scopes) {
        return String.join(" ", scopes);
    }

    public boolean grants(Set<String> granted, Set<String> required) {
        return granted.containsAll(required);
    }

    /**
     * A refresh may narrow the granted scopes but never widen them. An empty request keeps
     * everything the connection already carries.
     */
    public Set<String> negotiateRefreshScopes(Collection<String> grantedScopes, String requestedScope) {
        Set<String> granted = new LinkedHashSet<>(grantedScopes);
        Set<String> requested = parse(requestedScope);
        Set<String> effective = requested.isEmpty() ? granted : requested;
        if (!grants(granted, effective)) {
            throw new BusinessException("oauth.error.invalid-grant");
        }
        return effective;
    }
}
