package co.jinear.core.model.request.topic;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString(callSuper = true)
public class TopicInitializeRequest extends BaseRequest {
    @NotBlank
    private String workspaceId;
    @NotBlank
    private String name;
    @NotBlank
    private String tag;
    private String color;
}
