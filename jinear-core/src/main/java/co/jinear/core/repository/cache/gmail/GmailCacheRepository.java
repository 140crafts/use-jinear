package co.jinear.core.repository.cache.gmail;

import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;

public interface GmailCacheRepository {

    boolean hasCachedThreadsResponse(String gmailUserId, String nextPageToken);

    void cacheThreadDetailResponse(String gmailUserId, String nextPageToken, GmailListThreadsResponse
            gmailListThreadsResponse);

    GmailListThreadsResponse getCachedThreadsResponse(String gmailUserId, String nextPageToken);

    boolean hasCachedThreadDetailResponse(String googleUserId, String externalId);

    void cacheThreadDetailResponse(String googleUserId, String externalId, GmailThreadVo gmailThreadVo);

    GmailThreadVo getCachedThreadDetailResponse(String googleUserId, String externalId);
}
