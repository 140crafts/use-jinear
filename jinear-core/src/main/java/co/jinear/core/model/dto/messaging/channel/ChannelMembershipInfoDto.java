package co.jinear.core.model.dto.messaging.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelMembershipInfoDto {

    private PlainChannelDto channel;
    private Boolean isJoined = Boolean.FALSE;
}
