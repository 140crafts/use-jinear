package co.jinear.core.model.request.topic;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class RetrieveTopicListRequest extends BaseRequest {

    @NotEmpty
    private List<String> topicIds;
}
