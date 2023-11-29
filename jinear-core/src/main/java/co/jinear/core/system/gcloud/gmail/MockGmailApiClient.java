package co.jinear.core.system.gcloud.gmail;

import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "mock.gmail-client.enabled", havingValue = "true")
public class MockGmailApiClient implements GmailApiClient {

    public GmailListThreadsResponse listThreads(String userId, String accessToken, String pageToken) {
        return new GmailListThreadsResponse();
    }

    public GmailThreadVo getThread(String userId, String threadId, String accessToken) {
        return new GmailThreadVo();
    }
}
