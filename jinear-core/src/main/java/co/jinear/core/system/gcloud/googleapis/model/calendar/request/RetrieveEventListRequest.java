package co.jinear.core.system.gcloud.googleapis.model.calendar.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RetrieveEventListRequest {

    private String calendarSourceId;
    private String timeMin;
    private String timeMax;
    private Integer maxResults = 2500;
}
