package co.jinear.core.system.gcloud.googleapis;

import co.jinear.core.system.gcloud.googleapis.converter.BatchRequestConverter;
import co.jinear.core.system.gcloud.googleapis.converter.BatchResponseConverter;
import co.jinear.core.system.gcloud.googleapis.model.GmailMessageVo;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import co.jinear.core.system.gcloud.googleapis.model.RetrieveBatchRequestVo;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;
import co.jinear.core.system.gcloud.googleapis.model.calendar.response.GoogleCalendarEventListResponse;
import co.jinear.core.system.gcloud.googleapis.model.calendar.response.GoogleCalendarListResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(value = "mock.google-apis-client.enabled", havingValue = "false", matchIfMissing = true)
public class RestGoogleApisClient implements GoogleApisClient {

    private static final String AUTH_HEADER = "Bearer %s";
    private static final String GMAIL_BATCH = "batch/gmail/v1";
    private static final String CALENDAR_LIST = "calendar/v3/users/me/calendarList";
    private static final String CALENDAR_EVENT_LIST = "calendar/v3/calendars/{calendarId}/events?timeMin={timeMin}&timeMax={timeMax}&maxResults={maxResults}";

    private final RestTemplate googleApisRestTemplate;
    private final BatchRequestConverter batchRequestConverter;
    private final BatchResponseConverter batchResponseConverter;

    public List<GmailMessageVo> retrieveBatchMessages(String token, List<RetrieveBatchRequestVo> messageVoList) {
        String boundary = "batch-message-req";
        String body = batchRequestConverter.convertToMessagesRequestBody(messageVoList, boundary);
        HttpHeaders headers = retrieveHeaders(token);
        addBoundary(headers, boundary);

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
        HttpHeaders headers = retrieveHeaders(token);
        addBoundary(headers, boundary);

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = googleApisRestTemplate.postForEntity(GMAIL_BATCH, requestEntity, String.class);
        return batchResponseConverter.mapToThreadsResponse(response);
    }

    @Override
    public GoogleCalendarListResponse retrieveCalendarList(String token) {
        log.info("Retrieve calendar list has started.");
        HttpHeaders headers = retrieveHeaders(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<GoogleCalendarListResponse> response = googleApisRestTemplate
                .exchange(CALENDAR_LIST, HttpMethod.GET, requestEntity, GoogleCalendarListResponse.class);
        return response.getBody();
    }

    @Override
    public GoogleCalendarEventListResponse retrieveEventList(String token, RetrieveEventListRequest request) {
        log.info("Retrieve event list has started. request: {}", request);

        UriTemplateHandler template = googleApisRestTemplate.getUriTemplateHandler();
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("calendarId", request.getCalendarId());
        uriVariables.put("timeMin", request.getTimeMin());
        uriVariables.put("timeMax", request.getTimeMax());
        uriVariables.put("maxResults", request.getMaxResults().toString());
        URI uri = template.expand(CALENDAR_EVENT_LIST, uriVariables);

        HttpHeaders headers = retrieveHeaders(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<GoogleCalendarEventListResponse> response = googleApisRestTemplate
                .exchange(uri, HttpMethod.GET, requestEntity, GoogleCalendarEventListResponse.class);
        return response.getBody();
    }

    private HttpHeaders retrieveHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER.formatted(token));
        return headers;
    }

    private void addBoundary(HttpHeaders headers, String boundary) {
        String contentType = "%s; boundary=\"%s\"".formatted(MediaType.MULTIPART_MIXED_VALUE, boundary);
        headers.set(HttpHeaders.CONTENT_TYPE, contentType);
    }
}
