package co.jinear.core.model.dto.calendar;

import co.jinear.core.model.dto.account.AccountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarMemberDto {

    private String calendarMemberId;
    private String accountId;
    private String workspaceId;
    private String calendarId;
    private AccountDto account;
    private CalendarDto calendar;
}
