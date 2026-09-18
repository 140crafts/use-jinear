package co.jinear.core.validator.oauth;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthEnabledValidator {

    private final OauthProperties oauthProperties;

    public void validateOauthIsEnabled() {
        if (!oauthProperties.isUsable()) {
            throw new BusinessException("oauth.error.disabled");
        }
    }
}
