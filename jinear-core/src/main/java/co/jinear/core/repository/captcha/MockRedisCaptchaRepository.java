package co.jinear.core.repository.captcha;

import co.jinear.core.model.dto.captcha.CaptchaChallengeDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "true")
public class MockRedisCaptchaRepository implements CaptchaRepository {

    private static final ConcurrentHashMap<String, CaptchaChallengeDto> CAPTCHA_CHALLENGE_MAP = new ConcurrentHashMap<>();

    @Override
    public int incrementAndGetRequestCount(String key) {
        return 0;
    }

    @Override
    public void storeChallenge(String key, CaptchaChallengeDto captchaChallengeDto) {
        CAPTCHA_CHALLENGE_MAP.put(key, captchaChallengeDto);
    }

    @Override
    public CaptchaChallengeDto getChallenge(String key, String prefix) {
        return CAPTCHA_CHALLENGE_MAP.get(key + prefix);
    }
}
