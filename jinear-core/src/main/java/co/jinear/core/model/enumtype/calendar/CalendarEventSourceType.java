package co.jinear.core.model.enumtype.calendar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CalendarEventSourceType {
    TASK(0),
    GOOGLE_CALENDAR(1);

    private final int value;
}
