package co.jinear.core.model.response.calendar;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.calendar.CalendarMemberDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarMemberPaginatedResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<CalendarMemberDto> calendarMemberDtos;
}
