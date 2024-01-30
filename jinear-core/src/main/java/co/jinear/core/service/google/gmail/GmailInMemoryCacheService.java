package co.jinear.core.service.google.gmail;

import co.jinear.core.service.cache.InMemoryCacheService;
import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import co.jinear.core.system.gcloud.googleapis.model.RetrieveBatchRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailInMemoryCacheService {

    private static final Long TTL = 2L;
    private static final String APP = "jinear:";
    private static final String PREFIX = "gmail:";
    private static final String THREAD_DETAIL_LIST = "thread-detail-list:";
    private static final String THREAD_DETAIL = "thread-detail:";
    private static final String THREADS = "threads:";

    private final InMemoryCacheService inMemoryCacheService;

    public boolean hasCachedThreadsResponse(String gmailUserId, String nextPageToken) {
        String key = generateThreadKey(gmailUserId, nextPageToken);
        return Boolean.TRUE.equals(inMemoryCacheService.hasKey(key));
    }

    public void cacheThreadsResponse(String gmailUserId, String nextPageToken, GmailListThreadsResponse gmailListThreadsResponse) {
        log.info("Cache threads response has started.");
        String key = generateThreadKey(gmailUserId, nextPageToken);
        inMemoryCacheService.put(key, gmailListThreadsResponse, Duration.ofMinutes(TTL).toSeconds());
    }

    public GmailListThreadsResponse getCachedThreadsResponse(String gmailUserId, String nextPageToken) {
        log.info("Get cache threads response has started.");
        String key = generateThreadKey(gmailUserId, nextPageToken);
        return (GmailListThreadsResponse) inMemoryCacheService.get(key);
    }

    public boolean hasCachedThreadDetailListResponse(List<RetrieveBatchRequestVo> messageVoList) {
        String key = generateThreadDetailListKey(messageVoList);
        return Boolean.TRUE.equals(inMemoryCacheService.hasKey(key));
    }

    public void cacheThreadDetailListResponse(List<RetrieveBatchRequestVo> messageVoList, List<GmailThreadVo> gmailThreadVos) {
        log.info("Cache thread detail list response has started.");
        String key = generateThreadDetailListKey(messageVoList);
        inMemoryCacheService.put(key, gmailThreadVos, Duration.ofMinutes(TTL).toSeconds());
    }

    public List<GmailThreadVo> getCachedThreadDetailListResponse(List<RetrieveBatchRequestVo> messageVoList) {
        log.info("Get cache thread detail list response has started.");
        String key = generateThreadDetailListKey(messageVoList);
        return cast(inMemoryCacheService.get(key));
    }

    public boolean hasCachedThreadDetailResponse(String threadId, String googleUserId) {
        String key = generateThreadDetailKey(threadId, googleUserId);
        return Boolean.TRUE.equals(inMemoryCacheService.hasKey(key));
    }

    public GmailThreadVo getCachedThreadDetailResponse(String threadId, String googleUserId) {
        log.info("Get cache thread detail response has started.");
        String key = generateThreadDetailKey(threadId, googleUserId);
        return (GmailThreadVo) inMemoryCacheService.get(key);
    }

    public void cacheThreadDetailResponse(String threadId, String googleUserId, GmailThreadVo gmailThreadVo) {
        log.info("Cache thread detail response has started.");
        String key = generateThreadDetailKey(threadId, googleUserId);
        inMemoryCacheService.put(key, gmailThreadVo, Duration.ofMinutes(TTL).toSeconds());
    }

    private String generateThreadKey(String gmailUserId, String nextPageToken) {
        return APP + PREFIX + THREADS + gmailUserId + nextPageToken;
    }

    private String generateThreadDetailListKey(List<RetrieveBatchRequestVo> messageVoList) {
        String key = Optional.ofNullable(messageVoList)
                .map(List::toArray)
                .map(Objects::hash)
                .map(String::valueOf)
                .orElse(null);
        return APP + PREFIX + THREAD_DETAIL_LIST + key;
    }

    private String generateThreadDetailKey(String threadId, String googleUserId) {
        return APP + PREFIX + THREAD_DETAIL + threadId + googleUserId;
    }

    @SuppressWarnings("unchecked")
    private static <T extends List<?>> T cast(Object obj) {
        return (T) obj;
    }
}
