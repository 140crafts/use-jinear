package co.jinear.core.model.request.topic;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
