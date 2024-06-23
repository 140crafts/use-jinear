package co.jinear.core.model.dto.messaging.thread;

import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.messaging.channel.PlainChannelDto;
import co.jinear.core.model.dto.robot.RobotDto;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ThreadDto extends PlainThreadDto {

    private String threadId;
    private String ownerId;
    private String channelId;
    private ZonedDateTime lastActivityTime;
    private ThreadMessageInfoDto threadMessageInfo;
    @Nullable
    private PlainAccountProfileDto account;
    @Nullable
    private RobotDto robot;
    private PlainChannelDto channel;
}
