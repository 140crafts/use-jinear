package co.jinear.core.repository.cache.gmail;

import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "true")
public class MockGmailCacheRepository implements GmailCacheRepository {

    @Override
    public boolean hasCachedThreadsResponse(String gmailUserId, String nextPageToken) {
        return Boolean.FALSE;
    }

    @Override
    public void cacheThreadDetailResponse(String gmailUserId, String nextPageToken, GmailListThreadsResponse gmailListThreadsResponse) {
        log.info("[Mock] Gmail cache repository cache threads response has started. gmailUserId: {}, nextPageToken: {}", gmailUserId, nextPageToken);
    }

    @Override
    public GmailListThreadsResponse getCachedThreadsResponse(String gmailUserId, String nextPageToken) {
        return null;
    }

    @Override
    public boolean hasCachedThreadDetailResponse(String googleUserId, String externalId) {
        return Boolean.FALSE;
    }

    @Override
    public void cacheThreadDetailResponse(String googleUserId, String externalId, GmailThreadVo gmailThreadVo) {
        log.info("[Mock] Gmail cache repository cache thread info response has started. googleUserId: {}, externalId: {}", googleUserId, externalId);
    }

    @Override
    public GmailThreadVo getCachedThreadDetailResponse(String googleUserId, String externalId) {
        return null;
    }
}
