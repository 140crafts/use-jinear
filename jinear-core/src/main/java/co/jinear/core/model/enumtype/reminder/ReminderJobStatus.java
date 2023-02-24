package co.jinear.core.model.enumtype.reminder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReminderJobStatus {
    PENDING(0),
    COMPLETED(1),
    CANCELLED(-1);

    private int value;
}
