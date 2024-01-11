package co.jinear.core.repository;

import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.FilterSort;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import co.jinear.core.repository.criteriabuilder.TaskSearchCriteriaBuilder;
import com.google.common.collect.Lists;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static co.jinear.core.model.enumtype.team.TeamMemberRoleType.ADMIN;

@Repository
@RequiredArgsConstructor
public class TaskSearchRepository {

    private final EntityManager entityManager;
    private final TaskSearchCriteriaBuilder taskSearchCriteriaBuilder;

    public Page<Task> filterBy(TaskSearchFilterVo taskSearchFilterVo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> taskCriteriaQuery = criteriaBuilder.createQuery(Task.class);
        Root<Task> taskRoot = taskCriteriaQuery.from(Task.class);

        Predicate predicateForTeamsWithTaskVisibilityAllTeamMember = retrieveFilterPredicateListForTeamsWithTaskVisibilityVisibleToAllTeamMembers(taskSearchFilterVo, criteriaBuilder, taskRoot);
        Predicate predicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins = retrieveFilterPredicateListForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins(taskSearchFilterVo, criteriaBuilder, taskRoot);

        Predicate searchPredicate = null;
        if (Objects.nonNull(predicateForTeamsWithTaskVisibilityAllTeamMember) && Objects.nonNull(predicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins)) {
            searchPredicate = criteriaBuilder.or(predicateForTeamsWithTaskVisibilityAllTeamMember, predicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins);
        } else if (Objects.nonNull(predicateForTeamsWithTaskVisibilityAllTeamMember)) {
            searchPredicate = predicateForTeamsWithTaskVisibilityAllTeamMember;
        } else if (Objects.nonNull(predicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins)) {
            searchPredicate = predicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins;
        }

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Task> countRoot = countQuery.from(Task.class);
        countQuery.select(criteriaBuilder.count(countRoot));

        Predicate countPredicateForTeamsWithTaskVisibilityAllTeamMember = retrieveFilterPredicateListForTeamsWithTaskVisibilityVisibleToAllTeamMembers(taskSearchFilterVo, criteriaBuilder, countRoot);
        Predicate countPredicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins = retrieveFilterPredicateListForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins(taskSearchFilterVo, criteriaBuilder, countRoot);

        Predicate countPredicate = null;
        if (Objects.nonNull(countPredicateForTeamsWithTaskVisibilityAllTeamMember) && Objects.nonNull(countPredicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins)) {
            countPredicate = criteriaBuilder.or(countPredicateForTeamsWithTaskVisibilityAllTeamMember, countPredicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins);
        } else if (Objects.nonNull(countPredicateForTeamsWithTaskVisibilityAllTeamMember)) {
            countPredicate = countPredicateForTeamsWithTaskVisibilityAllTeamMember;
        } else if (Objects.nonNull(countPredicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins)) {
            countPredicate = countPredicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins;
        }

        return executeAndRetrievePageableResults(
                criteriaBuilder,
                taskCriteriaQuery,
                countQuery,
                taskRoot,
                searchPredicate,
                countPredicate,
                taskSearchFilterVo.getSort(),
                PageRequest.of(taskSearchFilterVo.getPage(), taskSearchFilterVo.getSize()));
    }

    private Predicate retrieveFilterPredicateListForTeamsWithTaskVisibilityVisibleToAllTeamMembers(TaskSearchFilterVo taskSearchFilterVo, CriteriaBuilder criteriaBuilder, Root<Task> taskRoot) {
        List<Predicate> predicateList = Lists.newArrayList();
        Map<TeamTaskVisibilityType, List<TeamMemberDto>> teamMemberMap = taskSearchFilterVo.getTeamMemberMap();
        List<TeamMemberDto> teamMemberDtos = teamMemberMap.get(TeamTaskVisibilityType.VISIBLE_TO_ALL_TEAM_MEMBERS);
        if (Objects.nonNull(teamMemberDtos)) {
            List<String> teamIdList = teamMemberDtos.stream().map(TeamMemberDto::getTeamId).toList();
            taskSearchCriteriaBuilder.addPassiveIdIsNull(criteriaBuilder, taskRoot, predicateList);
            taskSearchCriteriaBuilder.addWorkspaceId(taskSearchFilterVo.getWorkspaceId(), criteriaBuilder, taskRoot, predicateList);
            taskSearchCriteriaBuilder.addTeamIdList(teamIdList, criteriaBuilder, taskRoot, predicateList);
            taskSearchCriteriaBuilder.addTopicIdList(taskSearchFilterVo.getTopicIds(), criteriaBuilder, taskRoot, predicateList);
            taskSearchCriteriaBuilder.addOwnerIdList(taskSearchFilterVo.getOwnerIds(), criteriaBuilder, taskRoot, predicateList);
            taskSearchCriteriaBuilder.addAssignedToList(taskSearchFilterVo.getAssigneeIds(), criteriaBuilder, taskRoot, predicateList);
            taskSearchCriteriaBuilder.addWorkflowStatusIdList(taskSearchFilterVo.getWorkflowStatusIdList(), criteriaBuilder, taskRoot, predicateList);
            taskSearchCriteriaBuilder.addWorkflowStateGroupList(taskSearchFilterVo.getWorkflowStateGroups(), criteriaBuilder, taskRoot, predicateList);
            taskSearchCriteriaBuilder.addDatePredicates(taskSearchFilterVo.getTimespanStart(), taskSearchFilterVo.getTimespanEnd(), criteriaBuilder, taskRoot, predicateList);
            return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));
        }
        return null;
    }

    private Predicate retrieveFilterPredicateListForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins(TaskSearchFilterVo taskSearchFilterVo, CriteriaBuilder criteriaBuilder, Root<Task> taskRoot) {
        List<Predicate> mainPredicateList = Lists.newArrayList();

        Map<TeamTaskVisibilityType, List<TeamMemberDto>> teamMemberMap = taskSearchFilterVo.getTeamMemberMap();
        List<TeamMemberDto> teamMemberDtos = teamMemberMap.get(TeamTaskVisibilityType.OWNER_ASSIGNEE_AND_ADMINS);
        if (Objects.nonNull(teamMemberDtos)) {
            teamMemberDtos.forEach(teamMemberDto -> {
                List<Predicate> teamPredicateList = Lists.newArrayList();
                String accountId = teamMemberDto.getAccountId();
                TeamMemberRoleType memberRole = teamMemberDto.getRole();
                String teamId = teamMemberDto.getTeamId();

                List<String> ownerIds = taskSearchFilterVo.getOwnerIds();
                List<String> assigneeIds = taskSearchFilterVo.getAssigneeIds();
                if (!ADMIN.equals(memberRole)) {
                    if (Objects.nonNull(ownerIds) && Objects.nonNull(assigneeIds) && !ownerIds.isEmpty() && !assigneeIds.isEmpty()) {
                        //user trying to filter tasks owned AND assigned to him/her-self
                        ownerIds = List.of(accountId);
                        assigneeIds = List.of(accountId);
                        Predicate ownerIdPredicate = taskSearchCriteriaBuilder.getOwnerIdInListPredicate(ownerIds, criteriaBuilder, taskRoot);
                        Predicate assigneeIdPredicate = taskSearchCriteriaBuilder.getAssignedToInListPredicate(assigneeIds, criteriaBuilder, taskRoot);
                        Predicate ownerAndAssigneePredicate = criteriaBuilder.and(ownerIdPredicate, assigneeIdPredicate);
                        teamPredicateList.add(ownerAndAssigneePredicate);
                    } else if (Objects.nonNull(ownerIds) && !ownerIds.isEmpty()) {
                        //user trying to filter tasks owned by him/her-self
                        ownerIds = List.of(accountId);
                        Predicate ownerIdPredicate = taskSearchCriteriaBuilder.getOwnerIdInListPredicate(ownerIds, criteriaBuilder, taskRoot);
                        teamPredicateList.add(ownerIdPredicate);
                    } else if (Objects.nonNull(assigneeIds) && !assigneeIds.isEmpty()) {
                        //user trying to filter tasks assigned to him/her-self
                        assigneeIds = List.of(accountId);
                        Predicate assigneeIdPredicate = taskSearchCriteriaBuilder.getAssignedToInListPredicate(assigneeIds, criteriaBuilder, taskRoot);
                        teamPredicateList.add(assigneeIdPredicate);
                    } else {
                        //user trying to filter tasks owned OR assigned to him/her-self
                        ownerIds = List.of(accountId);
                        assigneeIds = List.of(accountId);
                        Predicate ownerIdPredicate = taskSearchCriteriaBuilder.getOwnerIdInListPredicate(ownerIds, criteriaBuilder, taskRoot);
                        Predicate assigneeIdPredicate = taskSearchCriteriaBuilder.getAssignedToInListPredicate(assigneeIds, criteriaBuilder, taskRoot);
                        Predicate ownerOrAssigneePredicate = criteriaBuilder.or(ownerIdPredicate, assigneeIdPredicate);
                        teamPredicateList.add(ownerOrAssigneePredicate);
                    }
                }

                taskSearchCriteriaBuilder.addPassiveIdIsNull(criteriaBuilder, taskRoot, teamPredicateList);
                taskSearchCriteriaBuilder.addWorkspaceId(taskSearchFilterVo.getWorkspaceId(), criteriaBuilder, taskRoot, teamPredicateList);
                taskSearchCriteriaBuilder.addTeamIdList(List.of(teamId), criteriaBuilder, taskRoot, teamPredicateList);
                taskSearchCriteriaBuilder.addTopicIdList(taskSearchFilterVo.getTopicIds(), criteriaBuilder, taskRoot, teamPredicateList);
                taskSearchCriteriaBuilder.addWorkflowStatusIdList(taskSearchFilterVo.getWorkflowStatusIdList(), criteriaBuilder, taskRoot, teamPredicateList);
                taskSearchCriteriaBuilder.addWorkflowStateGroupList(taskSearchFilterVo.getWorkflowStateGroups(), criteriaBuilder, taskRoot, teamPredicateList);
                taskSearchCriteriaBuilder.addDatePredicates(taskSearchFilterVo.getTimespanStart(), taskSearchFilterVo.getTimespanEnd(), criteriaBuilder, taskRoot, teamPredicateList);

                Predicate teamPredicate = criteriaBuilder.and(teamPredicateList.toArray(Predicate[]::new));
                mainPredicateList.add(teamPredicate);
            });
            return criteriaBuilder.or(mainPredicateList.toArray(Predicate[]::new));
        }
        return null;
    }

    @SuppressWarnings("java:S107")
    private Page<Task> executeAndRetrievePageableResults(CriteriaBuilder criteriaBuilder,
                                                         CriteriaQuery<Task> taskCriteriaQuery,
                                                         CriteriaQuery<Long> countQuery,
                                                         Root<Task> taskRoot,
                                                         Predicate searchPredicate,
                                                         Predicate countPredicate,
                                                         FilterSort filterSort,
                                                         Pageable pageable) {

        taskCriteriaQuery.where(searchPredicate);
        setOrder(criteriaBuilder, taskCriteriaQuery, taskRoot, filterSort);

        List<Task> result = entityManager.createQuery(taskCriteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        countQuery.where(countPredicate);
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(result, pageable, count);
    }

    private void setOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery<Task> taskCriteriaQuery, Root<Task> taskRoot, FilterSort filterSort) {
        if (FilterSort.IDATE_ASC.equals(filterSort)) {
            taskCriteriaQuery.orderBy(criteriaBuilder.asc(taskRoot.get("createdDate")));
        } else if (FilterSort.IDATE_DESC.equals(filterSort)) {
            taskCriteriaQuery.orderBy(criteriaBuilder.desc(taskRoot.get("createdDate")));
        } else if (FilterSort.ASSIGNED_DATE_DESC.equals(filterSort)) {
            taskCriteriaQuery.orderBy(criteriaBuilder.desc(taskRoot.get("assignedDate")));
        } else if (FilterSort.ASSIGNED_DATE_ASC.equals(filterSort)) {
            taskCriteriaQuery.orderBy(criteriaBuilder.asc(taskRoot.get("assignedDate")));
        }
    }
}
