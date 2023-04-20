package co.jinear.core.model.response.notification;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.notification.NotificationMessageDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationEventListingResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<NotificationMessageDto> eventDtoPage;
}
