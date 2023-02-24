package co.jinear.core.model.enumtype.reminder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepeatType {
    NONE(0),
    HOURLY(1),
    DAILY(2),
    WEEKLY(3),
    BIWEEKLY(4),
    MONTHLY(5),
    EVERY_3_MONTHS(6),
    EVERY_6_MONTHS(7),
    YEARLY(8);

    private int value;
}
