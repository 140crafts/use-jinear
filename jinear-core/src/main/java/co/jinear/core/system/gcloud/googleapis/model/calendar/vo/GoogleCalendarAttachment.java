package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarAttachment {
    private String fileUrl;
    private String title;
    private String mimeType;
    private String iconLink;
    private String fileId;
}
