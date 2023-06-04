package co.jinear.core.service.task;

import co.jinear.core.model.dto.task.TaskSearchResultDto;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.vo.task.TaskSearchVo;
import co.jinear.core.repository.TaskFtsSearchRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSearchService {

    private static final int PAGE_SIZE = 50;

    private final TaskFtsSearchRepository taskFtsSearchRepository;

    public Page<TaskSearchResultDto> searchTasks(TaskSearchVo taskSearchVo) {
        log.info("Searching tasks has started. {}", taskSearchVo);
        return taskFtsSearchRepository.searchAllTasks(taskSearchVo.getTitle() , taskSearchVo.getWorkspaceId(), taskSearchVo.getTeamId(), PageRequest.of(taskSearchVo.getPage(), PAGE_SIZE))
                .map(this::mapTupleToDto);
    }

    public TaskSearchResultDto mapTupleToDto(Tuple tuple) {
        return TaskSearchResultDto.builder()
                .taskId(tuple.get("task_id", String.class))
                .topicId(tuple.get("topic_id", String.class))
                .workspaceId(tuple.get("workspace_id", String.class))
                .teamId(tuple.get("team_id", String.class))
                .ownerId(tuple.get("owner_id", String.class))
                .workflowStatusId(tuple.get("workflow_status_id", String.class))
                .assignedTo(tuple.get("assigned_to", String.class))
                .assignedDate(Optional.of(tuple).map(t -> t.get("assigned_date", Timestamp.class)).map(Timestamp::toInstant).map(instant -> ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"))).orElse(null))
                .dueDate(Optional.of(tuple).map(t -> t.get("due_date", Timestamp.class)).map(Timestamp::toInstant).map(instant -> ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"))).orElse(null))
                .hasPreciseAssignedDate(tuple.get("has_precise_assigned_date", Byte.class) == 1)
                .hasPreciseDueDate(tuple.get("has_precise_due_date", Byte.class) == 1)
                .teamTagNo(tuple.get("team_tag_no", Integer.class))
                .topicTagNo(tuple.get("topic_tag_no", Integer.class))
                .title(tuple.get("title", String.class))
                .teamTag(tuple.get("tag", String.class))
                .workflowStateName(tuple.get("workflowStateName", String.class))
                .workflowStateGroup(EnumUtils.getEnum(TeamWorkflowStateGroup.class, tuple.get("workflowStateGroup", String.class)))
                .build();
    }
}