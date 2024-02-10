package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarGadget {
    private String type;
    private String title;
    private String link;
    private String iconLink;
    private Integer width;
    private Integer height;
    private String display;
    private Map<String,String> preferences;
}
