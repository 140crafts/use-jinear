package co.jinear.core.service.google;

import co.jinear.core.system.gcloud.gmail.GmailApiClient;
import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import co.jinear.core.system.gcloud.googleapis.GoogleApisClient;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import co.jinear.core.system.gcloud.googleapis.model.RetrieveBatchRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailApiCallerService {

    private final GmailApiClient gmailApiClient;
    private final GoogleApisClient googleApisClient;

    public GmailListThreadsResponse listThreads(String googleUserId, String accessToken, String pageToken) {
        log.info("List threads has started. googleUserId: {}", googleUserId);
        return gmailApiClient.listThreads(googleUserId, accessToken, pageToken);
    }

    public GmailThreadVo getThread(String googleUserId, String threadId, String accessToken) {
        log.info("Get thread has started. googleUserId: {}, threadId: {}", googleUserId, threadId);
        return gmailApiClient.getThread(googleUserId, threadId, accessToken);
    }

    public List<GmailThreadVo> getBatchThreads(String token, List<RetrieveBatchRequestVo> messageVoList) {
        log.info("Get batch threads has started.");
        return googleApisClient.retrieveBatchThreads(token, messageVoList);
    }
}
