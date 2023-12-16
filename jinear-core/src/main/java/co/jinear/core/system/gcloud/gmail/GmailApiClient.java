package co.jinear.core.system.gcloud.gmail;

import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;

public interface GmailApiClient {

    GmailListThreadsResponse listThreads(String userId, String accessToken, String pageToken);

    GmailThreadVo getThread(String userId, String threadId, String accessToken);
}
