package co.jinear.core.system.gcloud.gmail;

import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "mock.gmail-client.enabled", havingValue = "true")
public class MockGmailApiClient implements GmailApiClient {

    public GmailListThreadsResponse listThreads(String userId, String accessToken) {
        return new GmailListThreadsResponse();
    }
}
