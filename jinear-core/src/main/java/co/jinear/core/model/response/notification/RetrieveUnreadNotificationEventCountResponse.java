package co.jinear.core.model.response.notification;

import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetrieveUnreadNotificationEventCountResponse extends BaseResponse {
    @NotNull
    Long unreadNotificationCount;
}
