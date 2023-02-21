package co.jinear.core.model.enumtype.reminder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepeatType {
    NONE(0),
    DAILY(1),
    WEEKLY(2),
    BIWEEKLY(3),
    MONTHLY(4),
    EVERY_3_MONTHS(5),
    EVERY_6_MONTHS(6),
    YEARLY(7);

    private int value;
}
