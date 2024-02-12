package co.jinear.core.model.enumtype.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskSource {

    GOOGLE_CALENDAR(1);

    private final int value;
}
