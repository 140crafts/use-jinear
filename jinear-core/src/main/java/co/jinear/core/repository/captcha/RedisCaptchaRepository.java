package co.jinear.core.repository.captcha;

import co.jinear.core.model.dto.captcha.CaptchaChallengeDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class RedisCaptchaRepository implements CaptchaRepository {

    private static final long REQ_COUNT_WINDOW = 1L;
    private static final long CHALLENGE_TTL = 2L;

    private static final String REQ_COUNT_KEY = "captcha:req-count:%s";
    private static final String CHALLENGE_KEY = "captcha:challenge:%s:%s";

    private final RedisTemplate<String, String> redisTemplate;
    private final Gson gson;

    @Override
    public int incrementAndGetRequestCount(String key) {
        log.info("Increment and get request count has started. key: {}", key);
        String reqCountKey = String.format(REQ_COUNT_KEY, key);
        RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger(reqCountKey, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        redisAtomicInteger.expire(REQ_COUNT_WINDOW, TimeUnit.MINUTES);
        return redisAtomicInteger.incrementAndGet();
    }

    @Override
    public void storeChallenge(String key, CaptchaChallengeDto captchaChallengeDto) {
        String json = gson.toJson(captchaChallengeDto);
        String prefixesJoin = StringUtils.join(captchaChallengeDto.getPrefixes());
        String challengeKey = String.format(CHALLENGE_KEY, key, prefixesJoin);
        redisTemplate.opsForValue().set(challengeKey, json, Duration.ofMinutes(CHALLENGE_TTL));
    }

    @Override
    public CaptchaChallengeDto getChallenge(String key, String prefixesJoin) {
        String challengeKey = String.format(CHALLENGE_KEY, key, prefixesJoin);
        String json = redisTemplate.opsForValue().get(challengeKey);
        return gson.fromJson(json, CaptchaChallengeDto.class);
    }
}
