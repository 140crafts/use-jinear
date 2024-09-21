package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TaskFtsSearchVo {

    private String query;
    private String workspaceId;
    private List<String> visibleToAllTeamIds;
    private List<String> ownerOrAssigneeTeamIds;
    private String assignedTo;
    private String ownerId;
    private int page;
}
