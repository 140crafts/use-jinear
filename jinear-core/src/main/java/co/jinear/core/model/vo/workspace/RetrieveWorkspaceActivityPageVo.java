package co.jinear.core.model.vo.workspace;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RetrieveWorkspaceActivityPageVo {
    private String workspaceId;
    private List<String> teamIdList;
    private int page;
}
