package co.jinear.core.model.response.reminder;

import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReminderJobResponse extends BaseResponse {
    @JsonProperty("data")
    private ReminderJobDto reminderJobDto;
}
