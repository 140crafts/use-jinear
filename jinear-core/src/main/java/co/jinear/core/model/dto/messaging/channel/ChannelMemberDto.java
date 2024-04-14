package co.jinear.core.model.dto.messaging.channel;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.enumtype.messaging.ChannelMemberRoleType;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ChannelMemberDto extends BaseDto {

    private String channelMemberId;
    private String channelId;
    private String accountId;
    private ChannelMemberRoleType roleType;
    @Nullable
    private ZonedDateTime silentUntil;
    @Nullable
    private PlainAccountProfileDto account;
    private PlainChannelDto channel;
}
