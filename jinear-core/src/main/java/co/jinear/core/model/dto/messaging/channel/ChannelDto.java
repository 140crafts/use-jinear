package co.jinear.core.model.dto.messaging.channel;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ChannelDto extends PlainChannelDto {

    private Set<ChannelSettingsDto> settings;
    private Set<ChannelMemberDto> members;
}
