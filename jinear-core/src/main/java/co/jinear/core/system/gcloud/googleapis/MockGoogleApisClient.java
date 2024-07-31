package co.jinear.core.system.gcloud.googleapis;

import co.jinear.core.system.gcloud.googleapis.model.GmailMessageVo;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import co.jinear.core.system.gcloud.googleapis.model.RetrieveBatchRequestVo;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;
import co.jinear.core.system.gcloud.googleapis.model.calendar.response.GoogleCalendarEventListResponse;
import co.jinear.core.system.gcloud.googleapis.model.calendar.response.GoogleCalendarListResponse;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarEventInfo;
import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(value = "mock.google-apis-client.enabled", havingValue = "true")
public class MockGoogleApisClient implements GoogleApisClient {

    @Override
    public List<GmailMessageVo> retrieveBatchMessages(String token, List<RetrieveBatchRequestVo> messageVoList) {
        return Collections.emptyList();
    }

    @Override
    public List<GmailThreadVo> retrieveBatchThreads(String token, List<RetrieveBatchRequestVo> messageVoList) {
        return Collections.emptyList();
    }

    @Override
    public GoogleCalendarInfo retrieveCalendar(String token, String calendarId) {
        return new GoogleCalendarInfo();
    }

    @Override
    public GoogleCalendarListResponse retrieveCalendarList(String token) {
        GoogleCalendarListResponse googleCalendarListResponse = new GoogleCalendarListResponse();
        googleCalendarListResponse.setItems(Collections.emptyList());
        return googleCalendarListResponse;
    }

    @Override
    public GoogleCalendarEventListResponse retrieveEventList(String token, RetrieveEventListRequest request) {
        GoogleCalendarEventListResponse googleCalendarEventListResponse = new GoogleCalendarEventListResponse();
        googleCalendarEventListResponse.setItems(Collections.emptyList());
        return googleCalendarEventListResponse;
    }

    @Override
    public GoogleCalendarEventInfo retrieveEvent(String token, String calendarSourceId, String eventId) {
        return new GoogleCalendarEventInfo();
    }

    @Override
    public GoogleCalendarEventInfo updateEvent(String token, String calendarSourceId, GoogleCalendarEventInfo googleCalendarEventInfo) {
        return new GoogleCalendarEventInfo();
    }

    @Override
    public void deleteEvent(String token, String calendarSourceId, String eventId) {
        log.info("[MOCK] Delete event has started.");
    }

    @Override
    public GoogleCalendarEventInfo initializeEvent(String token, String calendarSourceId, GoogleCalendarEventInfo googleCalendarEventInfo) {
        return new GoogleCalendarEventInfo();
    }

    @Override
    public GoogleCalendarEventInfo moveEvent(String token, String calendarSourceId, String eventId, String targetCalendarSourceId) {
        return new GoogleCalendarEventInfo();
    }
}
