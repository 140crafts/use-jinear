package co.jinear.core.model.response.calendar;

import co.jinear.core.model.dto.calendar.IntegrationExternalCalendarSourceListDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CalendarSourceListResponse extends BaseResponse {

    @JsonProperty("data")
    private List<IntegrationExternalCalendarSourceListDto> integrationExternalCalendarSourceListDtos;
}
