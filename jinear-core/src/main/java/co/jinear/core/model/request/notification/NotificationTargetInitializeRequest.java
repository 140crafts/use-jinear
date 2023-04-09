package co.jinear.core.model.request.notification;

import co.jinear.core.model.enumtype.notification.NotificationTargetType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class NotificationTargetInitializeRequest extends BaseRequest {

    @NotBlank
    private String externalTargetId;
    @Nullable
    private NotificationTargetType targetType = NotificationTargetType.WEB;
}
