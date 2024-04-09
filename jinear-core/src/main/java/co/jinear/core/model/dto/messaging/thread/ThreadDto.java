package co.jinear.core.model.dto.messaging.thread;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountDto;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ThreadDto extends BaseDto {

    private String threadId;
    private String ownerId;
    private String channelId;
    private ZonedDateTime lastActivityTime;
    private ThreadMessageInfoDto threadMessageInfo;
    private PlainAccountDto account;
}
