package co.jinear.core.model.vo.workspace;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeleteWorkspaceMemberVo {
    private String accountId;
    private String workspaceId;
}
