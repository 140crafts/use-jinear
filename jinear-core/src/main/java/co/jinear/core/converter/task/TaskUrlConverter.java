package co.jinear.core.converter.task;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.model.dto.task.RelatedTaskDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TaskUrlConverter {

    private final FeProperties feProperties;

    @AfterMapping
    public void fillUrl(@MappingTarget TaskDto taskDto) {
        String url = taskUrl(taskDto);
        taskDto.setUrl(url);
    }

    @AfterMapping
    public void fillUrl(@MappingTarget RelatedTaskDto relatedTaskDto) {
        String url = taskUrl(relatedTaskDto.getWorkspace(), relatedTaskDto.getTeam(), relatedTaskDto.getTeamTagNo(), relatedTaskDto.getTaskId());
        relatedTaskDto.setUrl(url);
    }

    public String taskUrl(TaskDto task) {
        return taskUrl(task.getWorkspace(), task.getTeam(), task.getTeamTagNo(), task.getTaskId());
    }

    private String taskUrl(WorkspaceDto workspace, TeamDto team, Integer teamTagNo, String taskId) {
        String workspaceUsername = Objects.isNull(workspace) ? null : workspace.getUsername();
        String reference = Objects.isNull(team) || Objects.isNull(teamTagNo)
                ? taskId
                : team.getTag() + "-" + teamTagNo;
        return feProperties.getTaskUrl()
                .replace("{workspaceName}", Objects.isNull(workspaceUsername) ? "" : workspaceUsername)
                .replace("{taskTag}", reference);
    }
}
