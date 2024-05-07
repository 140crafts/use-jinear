package co.jinear.core.model.dto.messaging.channel;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ChannelInfoDto {

    private String channelId;
    @Nullable
    private ZonedDateTime lastChannelActivity;
}
