package co.jinear.core.system.gcloud.googleapis;

import co.jinear.core.system.gcloud.googleapis.model.GmailMessageVo;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import co.jinear.core.system.gcloud.googleapis.model.RetrieveBatchRequestVo;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;
import co.jinear.core.system.gcloud.googleapis.model.calendar.response.GoogleCalendarEventListResponse;
import co.jinear.core.system.gcloud.googleapis.model.calendar.response.GoogleCalendarListResponse;

import java.util.List;

public interface GoogleApisClient {

    List<GmailMessageVo> retrieveBatchMessages(String token, List<RetrieveBatchRequestVo> messageVoList);

    List<GmailThreadVo> retrieveBatchThreads(String token, List<RetrieveBatchRequestVo> messageVoList);

    GoogleCalendarListResponse retrieveCalendarList(String token);

    GoogleCalendarEventListResponse retrieveEventList(String token, RetrieveEventListRequest request);
}
