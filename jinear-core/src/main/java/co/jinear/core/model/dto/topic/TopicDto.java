package co.jinear.core.model.dto.topic;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.topic.TopicVisibility;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TopicDto extends BaseDto {
    private String topicId;
    private String workspaceId;
    private String ownerId;
    private String color;
    private String name;
    private String tag;
    private TopicVisibility visibility;
}
