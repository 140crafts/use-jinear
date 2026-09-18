package co.jinear.core.model.response.telemetry;

import co.jinear.core.model.dto.telemetry.InstanceReportResultDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InstanceReportResponse extends BaseResponse {

    @JsonProperty("data")
    private InstanceReportResultDto instanceReportResultDto;
}
