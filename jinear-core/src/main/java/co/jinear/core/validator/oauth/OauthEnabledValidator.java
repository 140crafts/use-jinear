package co.jinear.core.validator.oauth;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Checks that connecting applications is switched on for this instance.
 */
@Component
@RequiredArgsConstructor
public class OauthEnabledValidator {

    private final OauthProperties oauthProperties;

    public void validateOauthIsEnabled() {
        if (!Boolean.TRUE.equals(oauthProperties.getEnabled())) {
            throw new BusinessException("oauth.error.disabled");
        }
    }
}
