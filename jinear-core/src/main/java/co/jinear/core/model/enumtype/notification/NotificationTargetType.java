package co.jinear.core.model.enumtype.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationTargetType {

    WEB(0),
    WEBVIEW(1);

    private final int value;
}
