package co.jinear.core.service.task;

import co.jinear.core.model.dto.task.RelatedTaskDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.validator.task.TaskTeamVisibilityTypeAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskTeamVisibilityMaskService {

    private final TaskTeamVisibilityTypeAccessValidator validateTaskAccess;

    public TaskDto maskRelations(String accountId, TeamMemberDto teamMemberDto, TaskDto taskDto) {
        log.info("Mask relations has started. accountId: {}", accountId);
        Set<TaskRelationDto> relations = maskRelations(accountId, teamMemberDto, taskDto.getRelations());
        Set<TaskRelationDto> relatedIn = maskRelatedIn(accountId, teamMemberDto, taskDto.getRelatedIn());
        taskDto.setRelations(relations);
        taskDto.setRelatedIn(relatedIn);
        return taskDto;
    }

    private Set<TaskRelationDto> maskRelatedIn(String accountId, TeamMemberDto teamMemberDto, Set<TaskRelationDto> relations) {
        if (Objects.nonNull(relations)) {
            Set<TaskRelationDto> filteredRelations = new HashSet<>();
            relations.forEach(taskRelationDto -> {
                RelatedTaskDto relatedTaskDto = taskRelationDto.getTask();
                String relatedTaskOwnerId = Optional.ofNullable(relatedTaskDto).map(RelatedTaskDto::getOwnerId).orElse(null);
                String relatedTaskAssignedTo = Optional.ofNullable(relatedTaskDto).map(RelatedTaskDto::getAssignedTo).orElse(null);
                if (validateTaskAccess.hasTaskAccess(accountId, teamMemberDto, relatedTaskOwnerId, relatedTaskAssignedTo)) {
                    filteredRelations.add(taskRelationDto);
                } else {
                    TaskRelationDto maskedRelation = new TaskRelationDto();
                    maskedRelation.setTaskRelationId(taskRelationDto.getTaskRelationId());
                    maskedRelation.setRelationType(taskRelationDto.getRelationType());
                    filteredRelations.add(maskedRelation);
                }
            });
            return filteredRelations;
        }
        return Collections.emptySet();
    }

    private Set<TaskRelationDto> maskRelations(String accountId, TeamMemberDto teamMemberDto, Set<TaskRelationDto> relations) {
        if (Objects.nonNull(relations)) {
            Set<TaskRelationDto> filteredRelations = new HashSet<>();
            relations.forEach(taskRelationDto -> {
                RelatedTaskDto relatedTaskDto = taskRelationDto.getRelatedTask();
                String relatedTaskOwnerId = Optional.ofNullable(relatedTaskDto).map(RelatedTaskDto::getOwnerId).orElse(null);
                String relatedTaskAssignedTo = Optional.ofNullable(relatedTaskDto).map(RelatedTaskDto::getAssignedTo).orElse(null);
                if (validateTaskAccess.hasTaskAccess(accountId, teamMemberDto, relatedTaskOwnerId, relatedTaskAssignedTo)) {
                    filteredRelations.add(taskRelationDto);
                } else {
                    TaskRelationDto maskedRelation = new TaskRelationDto();
                    maskedRelation.setTaskRelationId(taskRelationDto.getTaskRelationId());
                    maskedRelation.setRelationType(taskRelationDto.getRelationType());
                    filteredRelations.add(maskedRelation);
                }
            });
            return filteredRelations;
        }
        return Collections.emptySet();
    }
}
