package co.jinear.core.system.gcloud.googleapis.model.calendar.response;

import co.jinear.core.system.gcloud.googleapis.model.calendar.vo.GoogleCalendarInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleCalendarListResponse {

    private String kind;
    private String etag;
    private String nextSyncToken;
    private List<GoogleCalendarInfo> items;
}
