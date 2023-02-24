package co.jinear.core.model.enumtype.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskReminderType {
    ASSIGNED_DATE(0),
    DUE_DATE(1),
    SPECIFIC_DATE(2);

    private int value;
}
