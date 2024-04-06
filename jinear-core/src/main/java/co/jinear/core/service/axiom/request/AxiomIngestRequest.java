package co.jinear.core.service.axiom.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Getter
@Setter
public class AxiomIngestRequest {

    @JsonProperty("_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime time = ZonedDateTime.now();
    @JsonProperty("data")
    private Object data;
}
