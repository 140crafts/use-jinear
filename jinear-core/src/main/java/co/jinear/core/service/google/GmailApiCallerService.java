package co.jinear.core.service.google;

import co.jinear.core.system.gcloud.gmail.GmailApiClient;
import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailApiCallerService {

    private final GmailApiClient gmailApiClient;

    public GmailListThreadsResponse listThreads(String googleUserId, String accessToken) {
        log.info("List threads has started. googleUserId: {}", googleUserId);
        return gmailApiClient.listThreads(googleUserId, accessToken);
    }
}
