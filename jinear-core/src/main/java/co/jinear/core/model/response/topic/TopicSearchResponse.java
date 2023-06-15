package co.jinear.core.model.response.topic;

import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TopicSearchResponse extends BaseResponse {

    @JsonProperty("data")
    private List<TopicDto> topicDtoList;
}
