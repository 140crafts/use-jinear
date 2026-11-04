package co.jinear.core.converter.mcp;

import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.dto.task.ChecklistItemDto;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import co.jinear.core.model.mcp.view.McpActivityView;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class McpActivityViewConverter {

    public McpActivityView activity(WorkspaceActivityDto dto) {
        McpActivityView view = new McpActivityView();
        view.setActivityId(dto.getWorkspaceActivityId());
        view.setType(Optional.ofNullable(dto.getType()).map(Enum::name).orElse(null));
        view.setCreatedAt(instant(dto.getCreatedDate()));
        view.setPerformedBy(dto.getPerformedBy());
        view.setPerformedByUsername(username(dto.getPerformedByAccount()).orElse(null));
        view.setWorkspaceId(dto.getWorkspaceId());
        view.setTeamId(dto.getTeamId());
        view.setTaskId(dto.getTaskId());
        view.setTaskTitle(dto.getGroupTitle());
        view.setTaskUrl(dto.getGroupLink());
        view.setOldValue(oldValue(dto));
        view.setNewValue(newValue(dto));
        view.setSubject(subject(dto));
        return view;
    }

    private String oldValue(WorkspaceActivityDto dto) {
        return statusName(dto.getOldWorkflowStatusDto())
                .or(() -> topicName(dto.getOldTopicDto()))
                .or(() -> username(dto.getOldAssignedToAccount()))
                .or(() -> usernames(dto.getOldCollaborators()))
                .or(() -> relationType(dto.getOldTaskRelationDto()))
                .or(() -> state(dto, dto.getOldState()))
                .orElse(null);
    }

    private String newValue(WorkspaceActivityDto dto) {
        return statusName(dto.getNewWorkflowStatusDto())
                .or(() -> topicName(dto.getNewTopicDto()))
                .or(() -> username(dto.getNewAssignedToAccount()))
                .or(() -> usernames(dto.getNewCollaborators()))
                .or(() -> relationType(dto.getNewTaskRelationDto()))
                .or(() -> state(dto, dto.getNewState()))
                .orElse(null);
    }

    private String subject(WorkspaceActivityDto dto) {
        return Optional.ofNullable(dto.getRelatedChecklistItem()).map(ChecklistItemDto::getLabel)
                .or(() -> Optional.ofNullable(dto.getRelatedChecklist()).map(ChecklistDto::getTitle))
                .or(() -> Optional.ofNullable(dto.getTaskBoard()).map(TaskBoardDto::getTitle))
                .or(() -> Optional.ofNullable(dto.getRelatedTaskMedia()).map(MediaDto::getOriginalName))
                .or(() -> Optional.ofNullable(dto.getRelatedTask()).map(TaskDto::getTitle))
                .or(() -> username(dto.getRelatedAccount()))
                .orElse(null);
    }

    private Optional<String> state(WorkspaceActivityDto dto, String state) {
        if (WorkspaceActivityType.EDIT_TASK_DESC.equals(dto.getType())) {
            return Optional.empty();
        }
        return Optional.ofNullable(state);
    }

    private Optional<String> statusName(TeamWorkflowStatusDto dto) {
        return Optional.ofNullable(dto).map(TeamWorkflowStatusDto::getName);
    }

    private Optional<String> topicName(TopicDto dto) {
        return Optional.ofNullable(dto).map(TopicDto::getName);
    }

    private Optional<String> relationType(TaskRelationDto dto) {
        return Optional.ofNullable(dto).map(TaskRelationDto::getRelationType).map(Enum::name);
    }

    private Optional<String> username(PlainAccountProfileDto dto) {
        return Optional.ofNullable(dto).map(PlainAccountProfileDto::getUsername);
    }

    private Optional<String> usernames(Collection<PlainAccountProfileDto> accounts) {
        return Optional.ofNullable(accounts)
                .map(values -> values.stream().map(PlainAccountProfileDto::getUsername).collect(Collectors.joining(", ")));
    }

    private String instant(Date value) {
        return Optional.ofNullable(value).map(Date::toInstant).map(DateTimeFormatter.ISO_INSTANT::format).orElse(null);
    }
}
