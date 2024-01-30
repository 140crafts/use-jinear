package co.jinear.core.model.vo.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddCalendarMemberVo {

    private String accountId;
    private String workspaceId;
    private String calendarId;
}
