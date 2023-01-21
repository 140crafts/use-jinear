package co.jinear.core.service.workspace.activity;

import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.entity.workspace.WorkspaceActivity;
import co.jinear.core.model.vo.workspace.RetrieveTaskActivityVo;
import co.jinear.core.repository.WorkspaceActivityRepository;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.richtext.RichTextRetrieveService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusRetrieveService;
import co.jinear.core.service.topic.TopicRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static co.jinear.core.model.enumtype.workspace.WorkspaceActivityType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceActivityRetrieveService {

    private final WorkspaceActivityRepository workspaceActivityRepository;
    private final RichTextRetrieveService richTextRetrieveService;
    private final TeamWorkflowStatusRetrieveService teamWorkflowStatusRetrieveService;
    private final TopicRetrieveService topicRetrieveService;
    private final AccountRetrieveService accountRetrieveService;
    private final ModelMapper modelMapper;

    public List<WorkspaceActivityDto> retrieveTaskActivity(RetrieveTaskActivityVo retrieveTaskActivityVo) {
        log.info("Retrieve task activity has started. retrieveTaskActivityVo: {}", retrieveTaskActivityVo);
        List<WorkspaceActivity> workspaceActivities = workspaceActivityRepository.findAllByTaskIdAndPassiveIdIsNullOrderByCreatedDateAsc(retrieveTaskActivityVo.getTaskId());
        return workspaceActivities.stream()
                .map(workspaceActivity -> modelMapper.map(workspaceActivity, WorkspaceActivityDto.class))
                .map(this::retrieveTaskDescriptionChanges)
                .map(this::retrieveWorkflowStatusChanges)
                .map(this::retrieveTopicChanges)
                .map(this::retrieveAssigneeChanges)
                .toList();
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
}
