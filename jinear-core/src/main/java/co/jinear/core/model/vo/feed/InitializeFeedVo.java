package co.jinear.core.model.vo.feed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeFeedVo {

    private String workspaceId;
    private String initializedBy;
    private String integrationInfoId;
    private String name;
}
