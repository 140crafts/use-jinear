package co.jinear.core.model.dto.calendar;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CalendarShareKeyDto extends BaseDto {

    private String calendarShareKeyId;
    private String accountId;
    private String workspaceId;
    private String shareableKey;
}
