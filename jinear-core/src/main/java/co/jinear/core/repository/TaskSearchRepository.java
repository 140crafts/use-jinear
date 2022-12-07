package co.jinear.core.repository;

import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.vo.task.SearchIntersectingTasksVo;
import co.jinear.core.repository.criteriabuilder.TaskSearchCriteriaBuilder;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskSearchRepository {

    private final EntityManager entityManager;
    private final TaskSearchCriteriaBuilder taskSearchCriteriaBuilder;

    public List<Task> findAllIntersectingTasksFromWorkspaceAndTeamBetween(SearchIntersectingTasksVo searchIntersectingTasksVo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> taskCriteriaQuery = criteriaBuilder.createQuery(Task.class);
        Root<Task> taskRoot = taskCriteriaQuery.from(Task.class);
        List<Predicate> predicateList = retrievePredicateListForIntersectingTasks(searchIntersectingTasksVo, criteriaBuilder, taskRoot);
        taskCriteriaQuery
                .where(predicateList.toArray(Predicate[]::new))
                .orderBy(criteriaBuilder.asc(taskRoot.get("assignedDate")));
        return entityManager.createQuery(taskCriteriaQuery)
                .getResultList();
    }

    private List<Predicate> retrievePredicateListForIntersectingTasks(SearchIntersectingTasksVo searchIntersectingTasksVo, CriteriaBuilder criteriaBuilder, Root<Task> taskRoot) {
        List<Predicate> predicateList = Lists.newArrayList();
        taskSearchCriteriaBuilder.addWorkspaceId(searchIntersectingTasksVo.getWorkspaceId(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addTeamId(searchIntersectingTasksVo.getTeamId(), criteriaBuilder, taskRoot, predicateList);
        taskSearchCriteriaBuilder.addIntersectingTasksPredicates(searchIntersectingTasksVo.getTimespanStart(), searchIntersectingTasksVo.getTimespanEnd(), criteriaBuilder, taskRoot, predicateList);
        return predicateList;
    }
}
