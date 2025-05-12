package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarOfficeLocation {
    private String buildingId;
    private String floorId;
    private String floorSectionId;
    private String deskId;
    private String label;
}
