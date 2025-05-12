package co.jinear.core.model.vo.topic;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TopicInitializeVo {
    private String ownerId;
    private String workspaceId;
    private String teamId;
    private String color;
    private String name;
    private String tag;
}
