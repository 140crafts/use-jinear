package co.jinear.core.service.oauth.provider;

import co.jinear.core.model.enumtype.oauth.OauthScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
}
