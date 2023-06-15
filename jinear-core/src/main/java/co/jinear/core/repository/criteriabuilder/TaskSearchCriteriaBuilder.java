package co.jinear.core.repository.criteriabuilder;

import co.jinear.core.model.entity.task.Task;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class TaskSearchCriteriaBuilder {

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

    public void addWorkflowStatusIdList(List<String> workflowStatusIdList, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
        if (Objects.nonNull(workflowStatusIdList) && !workflowStatusIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("workflowStatusId"));
            workflowStatusIdList.forEach(in::value);
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

    public void addIntersectingTasksPredicates(ZonedDateTime start, ZonedDateTime end, CriteriaBuilder criteriaBuilder, Root<Task> root, List<Predicate> predicateList) {
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
}
