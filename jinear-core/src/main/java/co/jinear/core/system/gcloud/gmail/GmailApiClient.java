package co.jinear.core.system.gcloud.gmail;

import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;

public interface GmailApiClient {

    GmailListThreadsResponse listThreads(String userId, String accessToken);
}
