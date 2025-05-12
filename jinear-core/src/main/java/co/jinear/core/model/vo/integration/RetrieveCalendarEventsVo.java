package co.jinear.core.model.vo.integration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class RetrieveCalendarEventsVo {

    private String accountId;
    private ZonedDateTime timespanStart;
    private ZonedDateTime timespanEnd;
}
