package co.jinear.core.model.vo.topic;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class TopicDeleteVo {
    private String topicId;
    private String responsibleAccountId;
}
