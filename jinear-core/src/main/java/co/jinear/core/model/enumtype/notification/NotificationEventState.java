package co.jinear.core.model.enumtype.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationEventState {

    INITIALIZED(0),
    SENT(1);
    private int value;
}
