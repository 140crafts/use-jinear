package co.jinear.core.model.response.calendar;

import co.jinear.core.model.dto.calendar.CalendarShareKeyDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CalendarShareableKeyResponse extends BaseResponse {

    @JsonProperty("data")
    private CalendarShareKeyDto calendarShareKeyDto;
}
