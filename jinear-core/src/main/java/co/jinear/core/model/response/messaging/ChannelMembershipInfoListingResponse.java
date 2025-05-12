package co.jinear.core.model.response.messaging;

import co.jinear.core.model.dto.messaging.channel.ChannelMembershipInfoDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChannelMembershipInfoListingResponse extends BaseResponse {

    @JsonProperty("data")
    private List<ChannelMembershipInfoDto> channelMembershipInfoDtoList;
}
