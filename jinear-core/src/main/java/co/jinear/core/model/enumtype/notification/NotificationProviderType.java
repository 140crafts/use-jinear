package co.jinear.core.model.enumtype.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationProviderType {

    ONE_SIGNAL(0),
    FIREBASE(1),
    EXPO(2);

    private final int value;
}
