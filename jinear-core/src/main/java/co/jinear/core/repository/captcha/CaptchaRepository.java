package co.jinear.core.repository.captcha;

import co.jinear.core.model.dto.captcha.CaptchaChallengeDto;

public interface CaptchaRepository {

    int incrementAndGetRequestCount(String key);

    void storeChallenge(String key, CaptchaChallengeDto captchaChallengeDto);

    CaptchaChallengeDto getChallenge(String key, String prefixesJoin);
}
