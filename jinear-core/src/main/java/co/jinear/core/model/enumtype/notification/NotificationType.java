package co.jinear.core.model.enumtype.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    TASK_REMINDER(0),
    WORKSPACE_ACTIVITY(1);

    private final int value;
}
