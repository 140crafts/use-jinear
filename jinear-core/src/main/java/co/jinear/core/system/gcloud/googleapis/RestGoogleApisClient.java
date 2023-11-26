package co.jinear.core.system.gcloud.googleapis;

import co.jinear.core.system.gcloud.googleapis.converter.BatchRequestConverter;
import co.jinear.core.system.gcloud.googleapis.converter.BatchResponseConverter;
import co.jinear.core.system.gcloud.googleapis.model.GmailMessageVo;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import co.jinear.core.system.gcloud.googleapis.model.RetrieveBatchRequestVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(value = "mock.google-apis-client.enabled", havingValue = "false", matchIfMissing = true)
public class RestGoogleApisClient implements GoogleApisClient {

    private static final String AUTH_HEADER = "Bearer %s";
    private static final String GMAIL_BATCH = "batch/gmail/v1";

    private final RestTemplate googleApisRestTemplate;
    private final BatchRequestConverter batchRequestConverter;
    private final BatchResponseConverter batchResponseConverter;

    public List<GmailMessageVo> retrieveBatchMessages(String token, List<RetrieveBatchRequestVo> messageVoList) {
        String boundary = "batch-message-req";
        String body = batchRequestConverter.convertToMessagesRequestBody(messageVoList, boundary);
        HttpHeaders headers = retrieveHeaders(token, boundary);

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = googleApisRestTemplate.postForEntity(GMAIL_BATCH, requestEntity, String.class);
        return batchResponseConverter.mapToMessagesResponse(response);
    }

    public List<GmailThreadVo> retrieveBatchThreads(String token, List<RetrieveBatchRequestVo> messageVoList) {
        if (messageVoList.isEmpty()) {
            return Collections.emptyList();
        }
        String boundary = "batch-thread-req";
        String body = batchRequestConverter.convertToThreadsRequestBody(messageVoList, boundary);
        HttpHeaders headers = retrieveHeaders(token, boundary);

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = googleApisRestTemplate.postForEntity(GMAIL_BATCH, requestEntity, String.class);
        return batchResponseConverter.mapToThreadsResponse(response);
    }

    private HttpHeaders retrieveHeaders(String token, String boundary) {
        String contentType = "%s; boundary=\"%s\"".formatted(MediaType.MULTIPART_MIXED_VALUE, boundary);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, contentType);
        headers.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER.formatted(token));
        return headers;
    }


}
