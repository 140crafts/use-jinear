package co.jinear.core.model.request.topic;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString(callSuper = true)
public class TopicUpdateRequest extends BaseRequest {
    @NotBlank
    String topicId;
    private String color;
    private String name;
    private String tag;
}
