package co.jinear.core.repository.criteriabuilder;

import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.entity.task.TaskBoardEntry;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class TaskSearchCriteriaBuilder {

    public void addPassiveIdIsNull(CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        Predicate predicate = criteriaBuilder.isNull(root.<String>get("passiveId"));
        predicateList.add(predicate);
    }

    public void addWorkspaceId(String workspaceId, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(workspaceId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get("workspaceId"), workspaceId);
            predicateList.add(predicate);
        }
    }

    public void addTeamId(String teamId, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(teamId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get("teamId"), teamId);
            predicateList.add(predicate);
        }
    }

    public void addTeamIdList(List<String> teamIdList, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(teamIdList) && !teamIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("teamId"));
            teamIdList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public void addTeamIdNotInList(List<String> teamIdList, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(teamIdList) && !teamIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("teamId"));
            teamIdList.forEach(in::value);
            Predicate notIn = criteriaBuilder.not(in);
            predicateList.add(notIn);
        }
    }

    public void addTopicIdList(List<String> topicIdList, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(topicIdList) && !topicIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("topicId"));
            topicIdList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public void addOwnerIdList(List<String> ownerIdList, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(ownerIdList) && !ownerIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("ownerId"));
            ownerIdList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public Predicate getOwnerIdInListPredicate(List<String> ownerIdList, CriteriaBuilder criteriaBuilder, Root<Task> root) {
        if (Objects.nonNull(ownerIdList) && !ownerIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("ownerId"));
            ownerIdList.forEach(in::value);
            return in;
        }
        return null;
    }

    public void addWorkflowStatusIdList(List<String> workflowStatusIdList, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(workflowStatusIdList) && !workflowStatusIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("workflowStatusId"));
            workflowStatusIdList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public void addWorkflowStateGroupList(List<TeamWorkflowStateGroup> workflowStateGroups, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(workflowStateGroups) && !workflowStateGroups.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("workflowStatus").get("workflowStateGroup").as(String.class));
            workflowStateGroups.forEach(teamWorkflowStateGroup -> in.value(teamWorkflowStateGroup.toString()));
            predicateList.add(in);
        }
    }

    public void addAssignedToList(List<String> assignedToList, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(assignedToList) && !assignedToList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("assignedTo"));
            assignedToList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public Predicate getAssignedToInListPredicate(List<String> assignedToList, CriteriaBuilder criteriaBuilder, Root<Task> root) {
        if (Objects.nonNull(assignedToList) && !assignedToList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("assignedTo"));
            assignedToList.forEach(in::value);
            return in;
        }
        return null;
    }

    public void addDatePredicates(ZonedDateTime start, ZonedDateTime end, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(start) && Objects.nonNull(end)) {
            addIntersectingTasksPredicates(start, end, criteriaBuilder, root, predicateList);
        } else if (Objects.nonNull(start)) {
            Predicate assignedDateOrDueDateAfterPredicate = getAssignedDateOrDueDateAfterPredicate(start, criteriaBuilder, root);
            Optional.ofNullable(assignedDateOrDueDateAfterPredicate)
                    .ifPresent(predicateList::add);
        } else if (Objects.nonNull(end)) {
            Predicate assignedDateOrDueDateBeforePredicate = getAssignedDateOrDueDateBeforePredicate(end, criteriaBuilder, root);
            Optional.ofNullable(assignedDateOrDueDateBeforePredicate)
                    .ifPresent(predicateList::add);
        }
    }

    public void addProjectIdList(List<String> projectIdList, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(projectIdList) && !projectIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("projectId"));
            projectIdList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public void addMilestoneIdList(List<String> milestoneIdList, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(milestoneIdList) && !milestoneIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("milestoneId"));
            milestoneIdList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public void addTaskBoardIdList(List<String> taskBoardIds, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(taskBoardIds) && !taskBoardIds.isEmpty()) {
            Join<Task, TaskBoardEntry> boardEntryJoin = root.join("taskBoardEntries");
            predicateList.add(boardEntryJoin.get("taskBoardId").in(taskBoardIds));
        }
    }

    private void addIntersectingTasksPredicates(ZonedDateTime start, ZonedDateTime end, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(start) && Objects.nonNull(end)) {
            Predicate assignedDateBetweenPredicate = getAssignedDateBetweenPredicate(start, end, criteriaBuilder, root);
            Predicate dueDateBetweenPredicate = getDueDateBetweenPredicate(start, end, criteriaBuilder, root);
            Predicate tasksStartedBeforeAndEndedAfterPredicate = getTasksStartedBeforeAndEndedAfterPredicates(start, end, criteriaBuilder, root);
            Predicate predicate = criteriaBuilder.or(assignedDateBetweenPredicate, dueDateBetweenPredicate, tasksStartedBeforeAndEndedAfterPredicate);
            predicateList.add(predicate);
        }
    }

    private Predicate getAssignedDateBetweenPredicate(ZonedDateTime start, ZonedDateTime end, CriteriaBuilder criteriaBuilder, Root<Task> root) {
        if (Objects.nonNull(start) && Objects.nonNull(end)) {
            return criteriaBuilder.between(root.<ZonedDateTime>get("assignedDate"), start, end);
        }
        return null;
    }

    private Predicate getDueDateBetweenPredicate(ZonedDateTime start, ZonedDateTime end, CriteriaBuilder criteriaBuilder, Root<Task> root) {
        if (Objects.nonNull(start) && Objects.nonNull(end)) {
            return criteriaBuilder.between(root.<ZonedDateTime>get("dueDate"), start, end);
        }
        return null;
    }

    private Predicate getAssignedDateBeforePredicate(ZonedDateTime start, CriteriaBuilder criteriaBuilder, Root<Task> root) {
        if (Objects.nonNull(start)) {
            return criteriaBuilder.lessThanOrEqualTo(root.<ZonedDateTime>get("assignedDate"), start);
        }
        return null;
    }

    private Predicate getDueDateAfterPredicate(ZonedDateTime end, CriteriaBuilder criteriaBuilder, Root<Task> root) {
        if (Objects.nonNull(end)) {
            return criteriaBuilder.greaterThanOrEqualTo(root.<ZonedDateTime>get("dueDate"), end);
        }
        return null;
    }

    private Predicate getTasksStartedBeforeAndEndedAfterPredicates(ZonedDateTime start, ZonedDateTime end, CriteriaBuilder criteriaBuilder, Root<Task> root) {
        return criteriaBuilder.and(
                getAssignedDateBeforePredicate(start, criteriaBuilder, root),
                getDueDateAfterPredicate(end, criteriaBuilder, root)
        );
    }

    private Predicate getAssignedDateOrDueDateAfterPredicate(ZonedDateTime start, CriteriaBuilder criteriaBuilder, Root<Task> root) {
        if (Objects.nonNull(start)) {
            Predicate assignedDateAfter = criteriaBuilder.greaterThanOrEqualTo(root.<ZonedDateTime>get("assignedDate"), start);
            Predicate dueDateAfter = criteriaBuilder.greaterThanOrEqualTo(root.<ZonedDateTime>get("dueDate"), start);
            return criteriaBuilder.or(
                    assignedDateAfter,
                    dueDateAfter
            );
        }
        return null;
    }

    private Predicate getAssignedDateOrDueDateBeforePredicate(ZonedDateTime end, CriteriaBuilder criteriaBuilder, Root<Task> root) {
        if (Objects.nonNull(end)) {
            Predicate assignedDateBefore = criteriaBuilder.lessThanOrEqualTo(root.<ZonedDateTime>get("assignedDate"), end);
            Predicate dueDateBefore = criteriaBuilder.lessThanOrEqualTo(root.<ZonedDateTime>get("dueDate"), end);
            return criteriaBuilder.or(
                    assignedDateBefore,
                    dueDateBefore
            );
        }
        return null;
    }
}
