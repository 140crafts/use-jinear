package co.jinear.core.service.task;

import co.jinear.core.model.dto.task.TaskAnalyticNumbersDto;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.vo.task.TaskCountByWorkflowStatusGroupVo;
import co.jinear.core.repository.TaskAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static co.jinear.core.system.NormalizeHelper.COMMA_SEPARATOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskAnalyticsService {

    private final Long SIGHT = 3L;
    private final List<TeamWorkflowStateGroup> UNDONE_STATES = List.of(TeamWorkflowStateGroup.BACKLOG, TeamWorkflowStateGroup.NOT_STARTED, TeamWorkflowStateGroup.STARTED);
    private final List<TeamWorkflowStateGroup> CLOSED_STATES = List.of(TeamWorkflowStateGroup.COMPLETED, TeamWorkflowStateGroup.CANCELLED);

    private final TaskAnalyticsRepository taskAnalyticsRepository;

    public Long countAllByTeamId(String teamId) {
        log.info("Count all by team id has started for teamId: {}", teamId);
        Long count = taskAnalyticsRepository.countAllByTeamId(teamId);
        log.info("Found [{}] tasks with teamId: {}", count, teamId);
        return count;
    }

    public TaskAnalyticNumbersDto retrieveNumbers(String workspaceId, String teamId, ZonedDateTime relativeDate) {
        log.info("Retrieve numbers has started. workspaceId: {}, teamId: {}, relativeDate: {}", workspaceId, teamId, relativeDate);
        Long missedDeadlineCount = countTasksBeforeDateWithStatus(workspaceId, teamId, UNDONE_STATES, relativeDate);
        Long deadlineComingUpCount = countTasksWithinDateWithStatus(workspaceId, teamId, UNDONE_STATES, relativeDate, relativeDate.plusDays(SIGHT));
        Long totalOpenTaskCount = countTasksWithStatus(workspaceId, teamId, UNDONE_STATES);
        Long totalClosedTaskCount = countTasksWithStatus(workspaceId, teamId, CLOSED_STATES);
        Long totalTaskCount = countTasksWithStatus(workspaceId, teamId, Arrays.stream(TeamWorkflowStateGroup.values()).toList());
        Map<TeamWorkflowStateGroup, Long> statusCounts = countTasksByWorkflowStatusGroups(workspaceId, teamId);
        return mapNumbersVo(missedDeadlineCount, deadlineComingUpCount, totalOpenTaskCount, totalClosedTaskCount, totalTaskCount, statusCounts);
    }

    private Long countTasksBeforeDateWithStatus(String workspaceId, String teamId, List<TeamWorkflowStateGroup> teamWorkflowStateGroup, ZonedDateTime beforeDate) {
        log.info("Count tasks before date with status has started. workspaceId: {}, teamId: {}, teamWorkflowStateGroupList: {}, beforeDate: {}", workspaceId, teamId, StringUtils.join(teamWorkflowStateGroup, COMMA_SEPARATOR), beforeDate);
        return taskAnalyticsRepository.countTasksBeforeDateWithStatus(workspaceId, teamId, teamWorkflowStateGroup, beforeDate);
    }

    private Long countTasksWithinDateWithStatus(String workspaceId, String teamId, List<TeamWorkflowStateGroup> teamWorkflowStateGroup, ZonedDateTime relativeDateStart, ZonedDateTime relativeDateEnd) {
        log.info("Count tasks within date with status has started. workspaceId: {}, teamId: {}, teamWorkflowStateGroupList: {}, relativeDateStart: {}, relativeDateEnd: {}", workspaceId, teamId, StringUtils.join(teamWorkflowStateGroup, COMMA_SEPARATOR), relativeDateStart, relativeDateEnd);
        return taskAnalyticsRepository.countTasksWithinDateWithStatus(workspaceId, teamId, teamWorkflowStateGroup, relativeDateStart, relativeDateEnd);
    }

    private Long countTasksWithStatus(String workspaceId, String teamId, List<TeamWorkflowStateGroup> workflowStateGroups) {
        log.info("Count tasks with status has started. workspaceId: {}, teamId: {}, teamWorkflowStateGroupList: {}", workspaceId, teamId, StringUtils.join(workflowStateGroups, COMMA_SEPARATOR));
        return taskAnalyticsRepository.countAllByWorkspaceIdAndTeamIdAndWorkflowStatus_WorkflowStateGroupIsInAndPassiveIdIsNull(workspaceId, teamId, workflowStateGroups);
    }

    private Map<TeamWorkflowStateGroup, Long> countTasksByWorkflowStatusGroups(String workspaceId, String teamId) {
        log.info("Count tasks by workflow status groups has started. workspaceId: {}, teamId: {}", workspaceId, teamId);
        List<TaskCountByWorkflowStatusGroupVo> taskCountByWorkflowStatusGroupVos = taskAnalyticsRepository.countTasksByWorkflowStatusGroups(workspaceId, teamId);
        Map<TeamWorkflowStateGroup, Long> statusCounts = new EnumMap<>(TeamWorkflowStateGroup.class);
        taskCountByWorkflowStatusGroupVos.forEach(taskCountByWorkflowStatusGroupVo -> {
            TeamWorkflowStateGroup workflowStateGroup = taskCountByWorkflowStatusGroupVo.getWorkflowStateGroup();
            Long count = statusCounts.getOrDefault(workflowStateGroup, 0L) + taskCountByWorkflowStatusGroupVo.getTaskCount();
            statusCounts.put(workflowStateGroup, count);
        });
        return statusCounts;
    }

    private TaskAnalyticNumbersDto mapNumbersVo(Long missedDeadlineCount, Long deadlineComingUpCount, Long totalOpenTaskCount, Long totalClosedTaskCount, Long totalTaskCount, Map<TeamWorkflowStateGroup, Long> statusCounts) {
        TaskAnalyticNumbersDto taskAnalyticNumbersDto = new TaskAnalyticNumbersDto();
        taskAnalyticNumbersDto.setMissedDeadlineCount(missedDeadlineCount);
        taskAnalyticNumbersDto.setDeadlineComingUpCount(deadlineComingUpCount);
        taskAnalyticNumbersDto.setTotalOpenTaskCount(totalOpenTaskCount);
        taskAnalyticNumbersDto.setTotalClosedTaskCount(totalClosedTaskCount);
        taskAnalyticNumbersDto.setTotalTaskCount(totalTaskCount);
        taskAnalyticNumbersDto.setStatusCounts(statusCounts);
        return taskAnalyticNumbersDto;
    }
}
