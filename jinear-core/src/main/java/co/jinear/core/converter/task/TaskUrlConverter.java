package co.jinear.core.converter.task;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.model.dto.task.TaskDto;
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

    public String taskUrl(TaskDto task) {
        String workspaceUsername = Objects.isNull(task.getWorkspace()) ? null : task.getWorkspace().getUsername();
        String reference = Objects.isNull(task.getTeam()) || Objects.isNull(task.getTeamTagNo())
                ? task.getTaskId()
                : task.getTeam().getTag() + "-" + task.getTeamTagNo();
        return feProperties.getTaskUrl()
                .replace("{workspaceName}", Objects.isNull(workspaceUsername) ? "" : workspaceUsername)
                .replace("{taskTag}", reference);
    }
}
