package co.jinear.core.system.gcloud.googleapis.model.calendar.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleCalendarExtendedProperties {

    @JsonProperty("private")
    private Map<String, String> privateProperties;

    @JsonProperty("shared")
    private Map<String, String> sharedProperties;
}
