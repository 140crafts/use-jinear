package co.jinear.core.repository.cache.gmail;

import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import co.jinear.core.system.util.CryptoHelper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "false", matchIfMissing = true)
public class RedisGmailCacheRepository implements GmailCacheRepository {

    private static final Long TTL = 2L;
    private static final String APP = "jinear:";
    private static final String PREFIX = "gmail:";
    private static final String THREAD_DETAIL = "thread-detail:";
    private static final String THREADS = "threads:";
    private final RedisTemplate<String, String> redisTemplate;
    private final Gson gson;

    @Override
    public boolean hasCachedThreadsResponse(String gmailUserId, String nextPageToken) {
        String key = generateThreadKey(gmailUserId, nextPageToken);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }


    @Override
    public void cacheThreadDetailResponse(String gmailUserId, String nextPageToken, GmailListThreadsResponse gmailListThreadsResponse) {
        String json = gson.toJson(gmailListThreadsResponse);
        String encryptedResponse = CryptoHelper.encrypt(json);
        String key = generateThreadKey(gmailUserId, nextPageToken);
        redisTemplate.opsForValue().set(key, encryptedResponse, Duration.ofMinutes(TTL));
    }

    @Override
    public GmailListThreadsResponse getCachedThreadsResponse(String gmailUserId, String nextPageToken) {
        String key = generateThreadKey(gmailUserId, nextPageToken);
        String encryptedJson = redisTemplate.opsForValue().get(key);
        String json = CryptoHelper.decrypt(encryptedJson);
        return gson.fromJson(json, GmailListThreadsResponse.class);
    }

    @Override
    public boolean hasCachedThreadDetailResponse(String googleUserId, String externalId) {
        String key = generateThreadInfoKey(googleUserId, externalId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void cacheThreadDetailResponse(String googleUserId, String externalId, GmailThreadVo gmailThreadVo) {
        String json = gson.toJson(gmailThreadVo);
        String encryptedResponse = CryptoHelper.encrypt(json);
        String key = generateThreadInfoKey(googleUserId, externalId);
        redisTemplate.opsForValue().set(key, encryptedResponse, Duration.ofMinutes(TTL));
    }

    @Override
    public GmailThreadVo getCachedThreadDetailResponse(String googleUserId, String externalId) {
        String key = generateThreadInfoKey(googleUserId, externalId);
        String encryptedJson = redisTemplate.opsForValue().get(key);
        String json = CryptoHelper.decrypt(encryptedJson);
        return gson.fromJson(json, GmailThreadVo.class);
    }

    private String generateThreadKey(String gmailUserId, String nextPageToken) {
        return APP + PREFIX + THREADS + gmailUserId + nextPageToken;
    }

    private String generateThreadInfoKey(String googleUserId, String externalId) {
        return APP + PREFIX + THREAD_DETAIL + googleUserId + externalId;
    }
}
