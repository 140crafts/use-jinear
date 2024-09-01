package co.jinear.core.service.workspace.activity;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.task.TaskTagConverter;
import co.jinear.core.converter.workspace.WorkspaceActivityConverter;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.dto.task.ChecklistItemDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.entity.workspace.WorkspaceActivity;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.project.MilestoneRetrieveService;
import co.jinear.core.service.project.ProjectRetrieveService;
import co.jinear.core.service.richtext.RichTextRetrieveService;
import co.jinear.core.service.task.checklist.ChecklistItemService;
import co.jinear.core.service.task.checklist.ChecklistRetrieveService;
import co.jinear.core.service.task.media.TaskMediaRetrieveService;
import co.jinear.core.service.task.relation.TaskRelationRetrieveService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusRetrieveService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.system.NormalizeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static co.jinear.core.model.enumtype.workspace.WorkspaceActivityType.*;
import static co.jinear.core.system.NormalizeHelper.EMPTY_STRING;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceActivityDetailRetrieveService {

    private static final List<WorkspaceActivityType> CHECKLIST_RELATED_TYPES = List.of(
            CHECKLIST_INITIALIZED,
            CHECKLIST_REMOVED,
            CHECKLIST_TITLE_CHANGED);

    private static final List<WorkspaceActivityType> CHECKLIST_ITEM_RELATED_TYPES = List.of(
            CHECKLIST_ITEM_CHECKED_STATUS_CHANGED,
            CHECKLIST_ITEM_LABEL_CHANGED,
            CHECKLIST_ITEM_REMOVED,
            CHECKLIST_ITEM_INITIALIZED);

    private static final List<WorkspaceActivityType> TASK_MEDIA_RELATED_TYPES = List.of(
            ATTACHMENT_ADDED,
            ATTACHMENT_DELETED);

    private static final List<WorkspaceActivityType> GROUP_BY_TASK_TYPES = List.of(
            TASK_INITIALIZED,
            TASK_CLOSED,
            EDIT_TASK_TITLE,
            EDIT_TASK_DESC,
            TASK_UPDATE_TOPIC,
            TASK_UPDATE_WORKFLOW_STATUS,
            TASK_CHANGE_ASSIGNEE,
            TASK_CHANGE_ASSIGNED_DATE,
            TASK_CHANGE_DUE_DATE,
            RELATION_INITIALIZED,
            RELATION_REMOVED,
            CHECKLIST_INITIALIZED,
            CHECKLIST_REMOVED,
            CHECKLIST_TITLE_CHANGED,
            CHECKLIST_ITEM_CHECKED_STATUS_CHANGED,
            CHECKLIST_ITEM_LABEL_CHANGED,
            CHECKLIST_ITEM_REMOVED,
            CHECKLIST_ITEM_INITIALIZED,
            ATTACHMENT_ADDED,
            ATTACHMENT_DELETED,
            TASK_BOARD_ENTRY_INIT,
            TASK_BOARD_ENTRY_REMOVED,
            TASK_BOARD_ENTRY_ORDER_CHANGE,
            TASK_NEW_COMMENT,
            TASK_PROJECT_ASSIGNMENT_UPDATE,
            TASK_MILESTONE_ASSIGNMENT_UPDATE
    );

    private final RichTextRetrieveService richTextRetrieveService;
    private final TeamWorkflowStatusRetrieveService teamWorkflowStatusRetrieveService;
    private final TopicRetrieveService topicRetrieveService;
    private final AccountRetrieveService accountRetrieveService;
    private final TaskRelationRetrieveService taskRelationRetrieveService;
    private final WorkspaceActivityConverter workspaceActivityConverter;
    private final ChecklistRetrieveService checklistRetrieveService;
    private final ChecklistItemService checklistItemService;
    private final TaskTagConverter taskTagConverter;
    private final TaskMediaRetrieveService taskMediaRetrieveService;
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final TeamRetrieveService teamRetrieveService;
    private final FeProperties feProperties;
    private final ProjectRetrieveService projectRetrieveService;
    private final MilestoneRetrieveService milestoneRetrieveService;

    public WorkspaceActivityDto retrieveDetailsAndMap(WorkspaceActivity workspaceActivity) {
        return Optional.of(workspaceActivity)
                .map(workspaceActivityConverter::map)
                .map(this::retrieveWorkspace)
                .map(this::retrieveTeam)
                .map(this::retrieveTaskDescriptionChanges)
                .map(this::retrieveWorkflowStatusChanges)
                .map(this::retrieveTopicChanges)
                .map(this::retrieveAssigneeChanges)
                .map(this::retrieveRelationInitialized)
                .map(this::retrieveRelationRemoved)
                .map(this::retrieveRelatedChecklist)
                .map(this::retrieveRelatedChecklistItem)
                .map(this::retrieveRelatedTaskMedia)
                .map(this::retrieveProjectInfo)
                .map(this::retrieveMilestoneInfo)
                .map(this::decideGroupAttributes)
                .orElse(null);
    }

    private WorkspaceActivityDto retrieveWorkspace(WorkspaceActivityDto workspaceActivityDto) {
        String workspaceId = workspaceActivityDto.getWorkspaceId();
        if (Objects.nonNull(workspaceId)) {
            WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
            workspaceActivityDto.setWorkspaceDto(workspaceDto);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveTeam(WorkspaceActivityDto workspaceActivityDto) {
        String teamId = workspaceActivityDto.getTeamId();
        if (Objects.nonNull(teamId)) {
            TeamDto teamDto = teamRetrieveService.retrieveTeam(teamId);
            workspaceActivityDto.setTeamDto(teamDto);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveTaskDescriptionChanges(WorkspaceActivityDto workspaceActivityDto) {
        if (EDIT_TASK_DESC.equals(workspaceActivityDto.getType())) {
            String oldRichTextId = workspaceActivityDto.getOldState();
            String newRichTextId = workspaceActivityDto.getNewState();
            richTextRetrieveService.retrieveIncludingPassivesOptional(oldRichTextId).ifPresent(workspaceActivityDto::setOldDescription);
            richTextRetrieveService.retrieveIncludingPassivesOptional(newRichTextId).ifPresent(workspaceActivityDto::setNewDescription);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveWorkflowStatusChanges(WorkspaceActivityDto workspaceActivityDto) {
        if (TASK_UPDATE_WORKFLOW_STATUS.equals(workspaceActivityDto.getType())) {
            String oldWorkflowStatusId = workspaceActivityDto.getOldState();
            String newWorkflowStatusId = workspaceActivityDto.getNewState();
            teamWorkflowStatusRetrieveService.retrieveOptional(oldWorkflowStatusId).ifPresent(workspaceActivityDto::setOldWorkflowStatusDto);
            teamWorkflowStatusRetrieveService.retrieveOptional(newWorkflowStatusId).ifPresent(workspaceActivityDto::setNewWorkflowStatusDto);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveTopicChanges(WorkspaceActivityDto workspaceActivityDto) {
        if (TASK_UPDATE_TOPIC.equals(workspaceActivityDto.getType())) {
            String oldTopicId = workspaceActivityDto.getOldState();
            String newTopicId = workspaceActivityDto.getNewState();
            topicRetrieveService.retrieveOptional(oldTopicId).ifPresent(workspaceActivityDto::setOldTopicDto);
            topicRetrieveService.retrieveOptional(newTopicId).ifPresent(workspaceActivityDto::setNewTopicDto);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveAssigneeChanges(WorkspaceActivityDto workspaceActivityDto) {
        if (TASK_CHANGE_ASSIGNEE.equals(workspaceActivityDto.getType())) {
            String oldAssigneeId = workspaceActivityDto.getOldState();
            String newAssigneeId = workspaceActivityDto.getNewState();
            Optional.ofNullable(oldAssigneeId).map(accountRetrieveService::retrievePlainAccountProfile).ifPresent(workspaceActivityDto::setOldAssignedToAccount);
            Optional.ofNullable(newAssigneeId).map(accountRetrieveService::retrievePlainAccountProfile).ifPresent(workspaceActivityDto::setNewAssignedToAccount);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveRelationInitialized(WorkspaceActivityDto workspaceActivityDto) {
        if (RELATION_INITIALIZED.equals(workspaceActivityDto.getType())) {
            String relatedObjectId = workspaceActivityDto.getRelatedObjectId();
            TaskRelationDto taskRelationDto = taskRelationRetrieveService.retrieveTaskRelationInclPassive(relatedObjectId);
            workspaceActivityDto.setNewTaskRelationDto(taskRelationDto);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveRelationRemoved(WorkspaceActivityDto workspaceActivityDto) {
        if (RELATION_REMOVED.equals(workspaceActivityDto.getType())) {
            String relatedObjectId = workspaceActivityDto.getRelatedObjectId();
            TaskRelationDto taskRelationDto = taskRelationRetrieveService.retrieveTaskRelationInclPassive(relatedObjectId);
            workspaceActivityDto.setOldTaskRelationDto(taskRelationDto);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveRelatedChecklist(WorkspaceActivityDto workspaceActivityDto) {
        if (CHECKLIST_RELATED_TYPES.contains(workspaceActivityDto.getType())) {
            ChecklistDto checklistDto = checklistRetrieveService.retrieveIncludingPassive(workspaceActivityDto.getRelatedObjectId());
            workspaceActivityDto.setRelatedChecklist(checklistDto);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveRelatedChecklistItem(WorkspaceActivityDto workspaceActivityDto) {
        if (CHECKLIST_ITEM_RELATED_TYPES.contains(workspaceActivityDto.getType())) {
            ChecklistItemDto checklistItemDto = checklistItemService.retrieveIncludingPassive(workspaceActivityDto.getRelatedObjectId());
            workspaceActivityDto.setRelatedChecklistItem(checklistItemDto);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveRelatedTaskMedia(WorkspaceActivityDto workspaceActivityDto) {
        if (TASK_MEDIA_RELATED_TYPES.contains(workspaceActivityDto.getType())) {
            MediaDto mediaDto = taskMediaRetrieveService.retrieveInclDeleted(workspaceActivityDto.getTaskId(), workspaceActivityDto.getRelatedObjectId());
            workspaceActivityDto.setRelatedTaskMedia(mediaDto);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveProjectInfo(WorkspaceActivityDto workspaceActivityDto) {
        if (TASK_PROJECT_ASSIGNMENT_UPDATE.equals(workspaceActivityDto.getType())) {
            Optional.of(workspaceActivityDto)
                    .map(WorkspaceActivityDto::getOldState)
                    .map(projectRetrieveService::retrieveOptional)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .ifPresent(workspaceActivityDto::setOldProject);
            Optional.of(workspaceActivityDto)
                    .map(WorkspaceActivityDto::getNewState)
                    .map(projectRetrieveService::retrieveOptional)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .ifPresent(workspaceActivityDto::setNewProject);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto retrieveMilestoneInfo(WorkspaceActivityDto workspaceActivityDto) {
        if (TASK_MILESTONE_ASSIGNMENT_UPDATE.equals(workspaceActivityDto.getType())) {
            Optional.of(workspaceActivityDto)
                    .map(WorkspaceActivityDto::getOldState)
                    .map(milestoneRetrieveService::retrieveOptional)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .ifPresent(workspaceActivityDto::setOldMilestoneDto);
            Optional.of(workspaceActivityDto)
                    .map(WorkspaceActivityDto::getNewState)
                    .map(milestoneRetrieveService::retrieveOptional)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .ifPresent(workspaceActivityDto::setNewMilestoneDto);
        }
        return workspaceActivityDto;
    }

    private WorkspaceActivityDto decideGroupAttributes(WorkspaceActivityDto workspaceActivityDto) {
        if (GROUP_BY_TASK_TYPES.contains(workspaceActivityDto.getType())) {
            Optional.of(workspaceActivityDto)
                    .map(WorkspaceActivityDto::getRelatedTask)
                    .map(TaskDto::getTaskId)
                    .ifPresent(workspaceActivityDto::setGroupId);

            String taskTag = Optional.of(workspaceActivityDto)
                    .map(WorkspaceActivityDto::getRelatedTask)
                    .map(taskTagConverter::retrieveTaskTag)
                    .orElse(NormalizeHelper.EMPTY_STRING);
            String taskTitle = Optional.of(workspaceActivityDto)
                    .map(WorkspaceActivityDto::getRelatedTask)
                    .map(TaskDto::getTitle)
                    .orElse(NormalizeHelper.EMPTY_STRING);
            workspaceActivityDto.setGroupTitle("[" + taskTag + "]" + NormalizeHelper.SPACE_STRING + taskTitle);

            Optional.of(workspaceActivityDto)
                    .map(WorkspaceActivityDto::getRelatedTask)
                    .map(taskDto -> retrieveTaskUrl(taskDto, taskTag))
                    .ifPresent(workspaceActivityDto::setGroupLink);
        }
        return workspaceActivityDto;
    }

    private String retrieveTaskUrl(TaskDto taskDto, String taskTag) {
        String taskUrl = feProperties.getTaskUrl();
        String workspaceUsername = Optional.of(taskDto).map(TaskDto::getWorkspace).map(WorkspaceDto::getUsername).orElse(EMPTY_STRING);
        return taskUrl.replaceAll(Pattern.quote("{workspaceName}"), workspaceUsername)
                .replaceAll(Pattern.quote("{taskTag}"), taskTag);
    }
}
