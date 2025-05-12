package co.jinear.core.repository;

import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.entity.workspace.WorkspaceActivity;
import co.jinear.core.model.enumtype.FilterSort;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.vo.workspace.WorkspaceActivityFilterVo;
import co.jinear.core.repository.criteriabuilder.WorkspaceActivityCriteriaBuilder;
import com.google.common.collect.Lists;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
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
public class WorkspaceActivityFilterRepository {

    public static final int FILTER_PAGE_SIZE = 25;

    private final EntityManager entityManager;
    private final WorkspaceActivityCriteriaBuilder workspaceActivityCriteriaBuilder;

    public Page<WorkspaceActivity> filterBy(WorkspaceActivityFilterVo workspaceActivityFilterVo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WorkspaceActivity> criteriaQuery = criteriaBuilder.createQuery(WorkspaceActivity.class);
        Root<WorkspaceActivity> root = criteriaQuery.from(WorkspaceActivity.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<WorkspaceActivity> countRoot = countQuery.from(WorkspaceActivity.class);
        countQuery.select(criteriaBuilder.count(countRoot));

        Predicate searchPredicate = retrievePredicates(workspaceActivityFilterVo, criteriaBuilder, root);
        Predicate countPredicate = retrievePredicates(workspaceActivityFilterVo, criteriaBuilder, countRoot);

        int size = Math.max(1, Math.min(workspaceActivityFilterVo.getSize(), FILTER_PAGE_SIZE));
        return executeAndRetrievePageableResults(
                criteriaBuilder,
                criteriaQuery,
                countQuery,
                root,
                searchPredicate,
                countPredicate,
                workspaceActivityFilterVo.getTaskFilterSort(),
                PageRequest.of(workspaceActivityFilterVo.getPage(), size));
    }

    @Nullable
    private Predicate retrievePredicates(WorkspaceActivityFilterVo workspaceActivityFilterVo, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root) {
        Predicate predicateForTeamsWithTaskVisibilityAllTeamMember = retrieveFilterPredicateListForTeamsWithTaskVisibilityVisibleToAllTeamMembers(workspaceActivityFilterVo, criteriaBuilder, root);
        Predicate predicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins = retrieveFilterPredicateListForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins(workspaceActivityFilterVo, criteriaBuilder, root);

        if (Objects.nonNull(predicateForTeamsWithTaskVisibilityAllTeamMember) && Objects.nonNull(predicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins)) {
            return criteriaBuilder.or(predicateForTeamsWithTaskVisibilityAllTeamMember, predicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins);
        } else if (Objects.nonNull(predicateForTeamsWithTaskVisibilityAllTeamMember)) {
            return predicateForTeamsWithTaskVisibilityAllTeamMember;
        } else if (Objects.nonNull(predicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins)) {
            return predicateForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins;
        }
        return null;
    }

    private Predicate retrieveFilterPredicateListForTeamsWithTaskVisibilityVisibleToAllTeamMembers(WorkspaceActivityFilterVo workspaceActivityFilterVo, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root) {
        List<Predicate> predicateList = Lists.newArrayList();
        Map<TeamTaskVisibilityType, List<TeamMemberDto>> teamMemberMap = workspaceActivityFilterVo.getTeamMemberMap();
        List<TeamMemberDto> teamMemberDtos = teamMemberMap.get(TeamTaskVisibilityType.VISIBLE_TO_ALL_TEAM_MEMBERS);
        if (Objects.nonNull(teamMemberDtos)) {
            List<String> teamIdList = teamMemberDtos.stream().map(TeamMemberDto::getTeamId).toList();
            workspaceActivityCriteriaBuilder.addPassiveIdIsNull(criteriaBuilder, root, predicateList);
            workspaceActivityCriteriaBuilder.addWorkspaceId(workspaceActivityFilterVo.getWorkspaceId(), criteriaBuilder, root, predicateList);
            workspaceActivityCriteriaBuilder.addTeamIdList(teamIdList, criteriaBuilder, root, predicateList);
            workspaceActivityCriteriaBuilder.addOwnerIdList(workspaceActivityFilterVo.getOwnerIds(), criteriaBuilder, root, predicateList);
            workspaceActivityCriteriaBuilder.addAssignedToList(workspaceActivityFilterVo.getAssigneeIds(), criteriaBuilder, root, predicateList);
            workspaceActivityCriteriaBuilder.addTaskIdList(workspaceActivityFilterVo.getTaskIds(), criteriaBuilder, root, predicateList);
            return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));
        }
        return null;
    }

    private Predicate retrieveFilterPredicateListForTeamsWithTaskVisibilityOwnerAssigneeAndAdmins(WorkspaceActivityFilterVo workspaceActivityFilterVo, CriteriaBuilder criteriaBuilder, Root<WorkspaceActivity> root) {
        List<Predicate> mainPredicateList = Lists.newArrayList();

        Map<TeamTaskVisibilityType, List<TeamMemberDto>> teamMemberMap = workspaceActivityFilterVo.getTeamMemberMap();
        List<TeamMemberDto> teamMemberDtos = teamMemberMap.get(TeamTaskVisibilityType.OWNER_ASSIGNEE_AND_ADMINS);
        if (Objects.nonNull(teamMemberDtos)) {
            teamMemberDtos.forEach(teamMemberDto -> {
                List<Predicate> teamPredicateList = Lists.newArrayList();
                String accountId = teamMemberDto.getAccountId();
                TeamMemberRoleType memberRole = teamMemberDto.getRole();
                String teamId = teamMemberDto.getTeamId();

                List<String> ownerIds = workspaceActivityFilterVo.getOwnerIds();
                List<String> assigneeIds = workspaceActivityFilterVo.getAssigneeIds();
                if (!ADMIN.equals(memberRole)) {
                    if (Objects.nonNull(ownerIds) && Objects.nonNull(assigneeIds) && !ownerIds.isEmpty() && !assigneeIds.isEmpty()) {
                        //user trying to filter activities owned AND assigned to him/her-self
                        ownerIds = List.of(accountId);
                        assigneeIds = List.of(accountId);
                        Predicate ownerIdPredicate = workspaceActivityCriteriaBuilder.getOwnerIdInListPredicate(ownerIds, criteriaBuilder, root);
                        Predicate assigneeIdPredicate = workspaceActivityCriteriaBuilder.getAssignedToInListPredicate(assigneeIds, criteriaBuilder, root);
                        Predicate ownerAndAssigneePredicate = criteriaBuilder.and(ownerIdPredicate, assigneeIdPredicate);
                        teamPredicateList.add(ownerAndAssigneePredicate);
                    } else if (Objects.nonNull(ownerIds) && !ownerIds.isEmpty()) {
                        //user trying to filter activities owned by him/her-self
                        ownerIds = List.of(accountId);
                        Predicate ownerIdPredicate = workspaceActivityCriteriaBuilder.getOwnerIdInListPredicate(ownerIds, criteriaBuilder, root);
                        teamPredicateList.add(ownerIdPredicate);
                    } else if (Objects.nonNull(assigneeIds) && !assigneeIds.isEmpty()) {
                        //user trying to filter activities assigned to him/her-self
                        assigneeIds = List.of(accountId);
                        Predicate assigneeIdPredicate = workspaceActivityCriteriaBuilder.getAssignedToInListPredicate(assigneeIds, criteriaBuilder, root);
                        teamPredicateList.add(assigneeIdPredicate);
                    } else {
                        //user trying to filter activities owned OR assigned to him/her-self
                        ownerIds = List.of(accountId);
                        assigneeIds = List.of(accountId);
                        Predicate ownerIdPredicate = workspaceActivityCriteriaBuilder.getOwnerIdInListPredicate(ownerIds, criteriaBuilder, root);
                        Predicate assigneeIdPredicate = workspaceActivityCriteriaBuilder.getAssignedToInListPredicate(assigneeIds, criteriaBuilder, root);
                        Predicate ownerOrAssigneePredicate = criteriaBuilder.or(ownerIdPredicate, assigneeIdPredicate);
                        teamPredicateList.add(ownerOrAssigneePredicate);
                    }
                }

                workspaceActivityCriteriaBuilder.addPassiveIdIsNull(criteriaBuilder, root, teamPredicateList);
                workspaceActivityCriteriaBuilder.addWorkspaceId(workspaceActivityFilterVo.getWorkspaceId(), criteriaBuilder, root, teamPredicateList);
                workspaceActivityCriteriaBuilder.addTeamIdList(List.of(teamId), criteriaBuilder, root, teamPredicateList);
                workspaceActivityCriteriaBuilder.addTaskIdList(workspaceActivityFilterVo.getTaskIds(), criteriaBuilder, root, teamPredicateList);
                Predicate teamPredicate = criteriaBuilder.and(teamPredicateList.toArray(Predicate[]::new));
                mainPredicateList.add(teamPredicate);
            });
            return criteriaBuilder.or(mainPredicateList.toArray(Predicate[]::new));
        }
        return null;
    }

    private void setOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery<WorkspaceActivity> criteriaQuery, Root<WorkspaceActivity> root, FilterSort filterSort) {
        if (FilterSort.IDATE_ASC.equals(filterSort)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("createdDate")));
        } else if (FilterSort.IDATE_DESC.equals(filterSort)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));
        } else if (FilterSort.ASSIGNED_DATE_DESC.equals(filterSort)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("assignedDate")));
        } else if (FilterSort.ASSIGNED_DATE_ASC.equals(filterSort)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("assignedDate")));
        }
    }

    @SuppressWarnings("java:S107")
    private Page<WorkspaceActivity> executeAndRetrievePageableResults(CriteriaBuilder criteriaBuilder,
                                                                      CriteriaQuery<WorkspaceActivity> criteriaQuery,
                                                                      CriteriaQuery<Long> countQuery,
                                                                      Root<WorkspaceActivity> root,
                                                                      Predicate searchPredicate,
                                                                      Predicate countPredicate,
                                                                      FilterSort filterSort,
                                                                      Pageable pageable) {

        criteriaQuery.where(searchPredicate);
        setOrder(criteriaBuilder, criteriaQuery, root, filterSort);

        List<WorkspaceActivity> result = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        countQuery.where(countPredicate);
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(result, pageable, count);
    }
}
