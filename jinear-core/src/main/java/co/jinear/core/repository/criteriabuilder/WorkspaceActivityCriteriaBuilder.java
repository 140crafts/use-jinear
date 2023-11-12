package co.jinear.core.repository.criteriabuilder;

import co.jinear.core.model.entity.workspace.WorkspaceActivity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class WorkspaceActivityCriteriaBuilder {

    public void addPassiveIdIsNull(CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root, List<Predicate> predicateList) {
        Predicate predicate = criteriaBuilder.isNull(root.<String>get("passiveId"));
        predicateList.add(predicate);
    }

    public void addWorkspaceId(String workspaceId, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root, List<Predicate> predicateList) {
        if (Objects.nonNull(workspaceId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get("workspaceId"), workspaceId);
            predicateList.add(predicate);
        }
    }

    public void addTeamId(String teamId, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root, List<Predicate> predicateList) {
        if (Objects.nonNull(teamId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get("teamId"), teamId);
            predicateList.add(predicate);
        }
    }

    public void addTeamIdList(List<String> teamIdList, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root, List<Predicate> predicateList) {
        if (Objects.nonNull(teamIdList) && !teamIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("teamId"));
            teamIdList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public void addOwnerIdList(List<String> ownerIdList, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root, List<Predicate> predicateList) {
        if (Objects.nonNull(ownerIdList) && !ownerIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("relatedTask").get("ownerId"));
            ownerIdList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public void addTaskIdList(List<String> taskIdList, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root, List<Predicate> predicateList) {
        if (Objects.nonNull(taskIdList) && !taskIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("taskId"));
            taskIdList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public Predicate getOwnerIdInListPredicate(List<String> ownerIdList, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root) {
        if (Objects.nonNull(ownerIdList) && !ownerIdList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("relatedTask").get("ownerId"));
            ownerIdList.forEach(in::value);
            return in;
        }
        return null;
    }

    public void addAssignedToList(List<String> assignedToList, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root, List<Predicate> predicateList) {
        if (Objects.nonNull(assignedToList) && !assignedToList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("relatedTask").get("assignedTo"));
            assignedToList.forEach(in::value);
            predicateList.add(in);
        }
    }

    public Predicate getAssignedToInListPredicate(List<String> assignedToList, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root) {
        if (Objects.nonNull(assignedToList) && !assignedToList.isEmpty()) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("relatedTask").get("assignedTo"));
            assignedToList.forEach(in::value);
            return in;
        }
        return null;
    }
}
