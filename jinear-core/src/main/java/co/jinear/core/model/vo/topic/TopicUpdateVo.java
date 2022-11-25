package co.jinear.core.model.vo.topic;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TopicUpdateVo {
    private String topicId;
    private String color;
    private String name;
    private String tag;
}
