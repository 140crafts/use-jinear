package co.jinear.core.model.response.reminder;

import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReminderResponse extends BaseResponse {
    @JsonProperty("data")
    private List<ReminderDto> reminders;
}
