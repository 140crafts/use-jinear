package co.jinear.core.model.dto.messaging.thread;

import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ThreadDto extends PlainThreadDto {

    private String threadId;
    private String ownerId;
    private String channelId;
    private ZonedDateTime lastActivityTime;
    private ThreadMessageInfoDto threadMessageInfo;
    private PlainAccountProfileDto account;
    private PlainChannelDto channel;
}
