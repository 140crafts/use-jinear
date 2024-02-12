package co.jinear.core.model.response.calendar;

import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class CalendarEventListingResponse extends BaseResponse {

    @JsonProperty("data")
    private List<CalendarEventDto> calendarEventDtoList;
}
