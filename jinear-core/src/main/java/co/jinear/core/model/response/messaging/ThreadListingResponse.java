package co.jinear.core.model.response.messaging;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThreadListingResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<ThreadDto> threadsPage;
}
