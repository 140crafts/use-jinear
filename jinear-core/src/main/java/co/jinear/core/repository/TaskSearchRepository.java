package co.jinear.core.repository;

import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.vo.task.SearchIntersectingTasksFromTeamVo;
import co.jinear.core.model.vo.task.SearchIntersectingTasksFromWorkspaceVo;
import co.jinear.core.repository.criteriabuilder.TaskSearchCriteriaBuilder;
import com.google.common.collect.Lists;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskSearchRepository {

    private final EntityManager entityManager;
    private final TaskSearchCriteriaBuilder taskSearchCriteriaBuilder;

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

    private List<Predicate> retrievePredicateListForIntersectingTasks(SearchIntersectingTasksFromTeamVo searchIntersectingTasksFromTeamVo, CriteriaBuilder criteriaBuilder, Root<Task> taskRoot) {
        List<Predicate> predicateList = Lists.newArrayList();
        taskSearchCriteriaBuilder.addWorkspaceId(searchIntersectingTasksFromTeamVo.getWorkspaceId(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addTeamId(searchIntersectingTasksFromTeamVo.getTeamId(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addIntersectingTasksPredicates(searchIntersectingTasksFromTeamVo.getTimespanStart(), searchIntersectingTasksFromTeamVo.getTimespanEnd(), criteriaBuilder, taskRoot, predicateList);
        return predicateList;
    }

    private List<Predicate> retrievePredicateListForIntersectingTasks(SearchIntersectingTasksFromWorkspaceVo searchIntersectingTasksFromWorkspaceVo, CriteriaBuilder criteriaBuilder, Root<Task> taskRoot) {
        List<Predicate> predicateList = Lists.newArrayList();
        taskSearchCriteriaBuilder.addWorkspaceId(searchIntersectingTasksFromWorkspaceVo.getWorkspaceId(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addTeamIdList(searchIntersectingTasksFromWorkspaceVo.getTeamIdList(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addIntersectingTasksPredicates(searchIntersectingTasksFromWorkspaceVo.getTimespanStart(), searchIntersectingTasksFromWorkspaceVo.getTimespanEnd(), criteriaBuilder, taskRoot, predicateList);
        return predicateList;
    }

    private List<Task> executeAndRetrieveResults(CriteriaBuilder criteriaBuilder, CriteriaQuery<Task> taskCriteriaQuery, Root<Task> taskRoot, List<Predicate> predicateList) {
        taskCriteriaQuery
                .where(predicateList.toArray(Predicate[]::new))
                .orderBy(criteriaBuilder.asc(taskRoot.get("assignedDate")));
        return entityManager.createQuery(taskCriteriaQuery)
                .getResultList();
    }
}
