package co.jinear.core.model.dto.messaging.thread;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.messaging.ThreadType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class PlainThreadDto extends BaseDto {

    private String threadId;
    private ThreadType threadType;
    private String ownerId;
    private String channelId;
    private ZonedDateTime lastActivityTime;
}
