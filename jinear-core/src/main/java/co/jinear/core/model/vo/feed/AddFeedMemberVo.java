package co.jinear.core.model.vo.feed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddFeedMemberVo {

    private String accountId;
    private String workspaceId;
    private String feedId;
}
