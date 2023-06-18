package co.jinear.core.repository;

import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.vo.task.SearchIntersectingTasksFromTeamVo;
import co.jinear.core.model.vo.task.SearchIntersectingTasksFromWorkspaceVo;
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

@Repository
@RequiredArgsConstructor
public class TaskSearchRepository {

    private static final int PAGE_SIZE = 50;

    private final EntityManager entityManager;
    private final TaskSearchCriteriaBuilder taskSearchCriteriaBuilder;

    public Page<Task> filterBy(TaskSearchFilterVo taskSearchFilterVo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> taskCriteriaQuery = criteriaBuilder.createQuery(Task.class);
        Root<Task> taskRoot = taskCriteriaQuery.from(Task.class);

        List<Predicate> predicateList = retrieveFilterPredicateList(taskSearchFilterVo, criteriaBuilder, taskRoot);

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Task> countRoot = countQuery.from(Task.class);
        countQuery.select(criteriaBuilder.count(countRoot));
        List<Predicate> countPredicateList = retrieveFilterPredicateList(taskSearchFilterVo, criteriaBuilder, countRoot);

        return executeAndRetrievePageableResults(criteriaBuilder, taskCriteriaQuery, countQuery, taskRoot, predicateList, countPredicateList, PageRequest.of(taskSearchFilterVo.getPage(), PAGE_SIZE));
    }

    public List<Task> findAllIntersectingTasksFromWorkspaceAndTeamListBetween(SearchIntersectingTasksFromWorkspaceVo searchIntersectingTasksFromWorkspaceVo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> taskCriteriaQuery = criteriaBuilder.createQuery(Task.class);
        Root<Task> taskRoot = taskCriteriaQuery.from(Task.class);
        List<Predicate> predicateList = retrievePredicateListForIntersectingTasks(searchIntersectingTasksFromWorkspaceVo, criteriaBuilder, taskRoot);
        return executeAndRetrieveResults(criteriaBuilder, taskCriteriaQuery, taskRoot, predicateList);
    }

    public List<Task> findAllIntersectingTasksFromWorkspaceAndTeamBetween(SearchIntersectingTasksFromTeamVo searchIntersectingTasksFromTeamVo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> taskCriteriaQuery = criteriaBuilder.createQuery(Task.class);
        Root<Task> taskRoot = taskCriteriaQuery.from(Task.class);
        List<Predicate> predicateList = retrievePredicateListForIntersectingTasks(searchIntersectingTasksFromTeamVo, criteriaBuilder, taskRoot);
        return executeAndRetrieveResults(criteriaBuilder, taskCriteriaQuery, taskRoot, predicateList);
    }

    private List<Predicate> retrieveFilterPredicateList(TaskSearchFilterVo taskSearchFilterVo, CriteriaBuilder criteriaBuilder, Root<Task> taskRoot) {
        List<Predicate> predicateList = Lists.newArrayList();
        taskSearchCriteriaBuilder.addWorkspaceId(taskSearchFilterVo.getWorkspaceId(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addTeamIdList(taskSearchFilterVo.getTeamIdList(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addTopicIdList(taskSearchFilterVo.getTopicIds(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addOwnerIdList(taskSearchFilterVo.getOwnerIds(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addAssignedToList(taskSearchFilterVo.getAssigneeIds(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addWorkflowStatusIdList(taskSearchFilterVo.getWorkflowStatusIdList(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addDatePredicates(taskSearchFilterVo.getTimespanStart(), taskSearchFilterVo.getTimespanEnd(), criteriaBuilder, taskRoot, predicateList);
        return predicateList;
    }

    private List<Predicate> retrievePredicateListForIntersectingTasks(SearchIntersectingTasksFromTeamVo searchIntersectingTasksFromTeamVo, CriteriaBuilder criteriaBuilder, Root<Task> taskRoot) {
        List<Predicate> predicateList = Lists.newArrayList();
        taskSearchCriteriaBuilder.addWorkspaceId(searchIntersectingTasksFromTeamVo.getWorkspaceId(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addTeamId(searchIntersectingTasksFromTeamVo.getTeamId(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addDatePredicates(searchIntersectingTasksFromTeamVo.getTimespanStart(), searchIntersectingTasksFromTeamVo.getTimespanEnd(), criteriaBuilder, taskRoot, predicateList);
        return predicateList;
    }

    private List<Predicate> retrievePredicateListForIntersectingTasks(SearchIntersectingTasksFromWorkspaceVo searchIntersectingTasksFromWorkspaceVo, CriteriaBuilder criteriaBuilder, Root<Task> taskRoot) {
        List<Predicate> predicateList = Lists.newArrayList();
        taskSearchCriteriaBuilder.addWorkspaceId(searchIntersectingTasksFromWorkspaceVo.getWorkspaceId(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addTeamIdList(searchIntersectingTasksFromWorkspaceVo.getTeamIdList(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addDatePredicates(searchIntersectingTasksFromWorkspaceVo.getTimespanStart(), searchIntersectingTasksFromWorkspaceVo.getTimespanEnd(), criteriaBuilder, taskRoot, predicateList);
        return predicateList;
    }

    private List<Task> executeAndRetrieveResults(CriteriaBuilder criteriaBuilder, CriteriaQuery<Task> taskCriteriaQuery, Root<Task> taskRoot, List<Predicate> predicateList) {
        taskCriteriaQuery
                .where(predicateList.toArray(Predicate[]::new))
                .orderBy(criteriaBuilder.asc(taskRoot.get("assignedDate")));
        return entityManager.createQuery(taskCriteriaQuery)
                .getResultList();
    }

    private Page<Task> executeAndRetrievePageableResults(CriteriaBuilder criteriaBuilder, CriteriaQuery<Task> taskCriteriaQuery, CriteriaQuery<Long> countQuery, Root<Task> taskRoot, List<Predicate> predicateList, List<Predicate> countPredicateList, Pageable pageable) {
        taskCriteriaQuery
                .where(predicateList.toArray(Predicate[]::new))
                .orderBy(criteriaBuilder.asc(taskRoot.get("assignedDate")));
        List<Task> result = entityManager.createQuery(taskCriteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        countQuery.where(countPredicateList.toArray(Predicate[]::new));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl(result, pageable, count);
    }
}
