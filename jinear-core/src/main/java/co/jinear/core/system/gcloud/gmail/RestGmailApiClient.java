package co.jinear.core.system.gcloud.gmail;

import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(value = "mock.gmail-client.enabled", havingValue = "false", matchIfMissing = true)
public class RestGmailApiClient implements GmailApiClient {

    private static final int MAX_RESULTS = 20;
    private static final String LIST_MESSAGES = "https://gmail.googleapis.com/gmail/v1/users/%s/messages?access_token=%s";
    private static final String LIST_THREADS = "https://gmail.googleapis.com/gmail/v1/users/%s/threads?access_token=%s";
    private static final String GET_MESSAGE = "https://gmail.googleapis.com/gmail/v1/users/%s/messages/%s?access_token=%s";
    private static final String GET_THREAD = "https://gmail.googleapis.com/gmail/v1/users/%s/threads/%s?access_token=%s";

    private final RestTemplate gmailRestTemplate;

    public GmailListThreadsResponse listThreads(String userId, String accessToken, String pageToken) {
        log.info("List threads has started.");
        String url = LIST_THREADS.formatted(userId, accessToken);
        url += "&maxResults=%s".formatted(MAX_RESULTS);
        if (Objects.nonNull(pageToken)) {
            url += "&pageToken=%s".formatted(pageToken);
        }
        return gmailRestTemplate.getForObject(url, GmailListThreadsResponse.class);
    }

    public GmailThreadVo getThread(String userId, String threadId, String accessToken) {
        log.info("Get thread has started.");
        String url = GET_THREAD.formatted(userId, threadId, accessToken);
        return gmailRestTemplate.getForObject(url, GmailThreadVo.class);
    }
}
