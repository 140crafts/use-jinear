package co.jinear.core.converter.mcp;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.model.dto.task.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class McpLinkConverter {

    private final FeProperties feProperties;

    public String taskUrl(String workspaceUsername, TaskDto task) {
        String reference = Objects.isNull(task.getTeam()) || Objects.isNull(task.getTeamTagNo())
                ? task.getTaskId()
                : task.getTeam().getTag() + "-" + task.getTeamTagNo();
        return feProperties.getTaskUrl()
                .replace("{workspaceName}", Objects.isNull(workspaceUsername) ? "" : workspaceUsername)
                .replace("{taskTag}", reference);
    }
}
