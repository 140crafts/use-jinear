package co.jinear.core.model.request.topic;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TopicInitializeRequest extends BaseRequest {
    @NotBlank
    private String workspaceId;
    @NotBlank
    private String teamId;
    @NotBlank
    private String name;
    @NotBlank
    private String tag;
    private String color;
}
