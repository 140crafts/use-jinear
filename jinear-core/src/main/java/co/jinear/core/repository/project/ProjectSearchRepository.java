package co.jinear.core.repository.project;

import co.jinear.core.model.entity.project.Project;
import co.jinear.core.repository.criteriabuilder.ProjectSearchCriteriaBuilder;
import com.google.common.collect.Lists;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectSearchRepository {

    private final EntityManager entityManager;
    private final ProjectSearchCriteriaBuilder projectSearchCriteriaBuilder;

    public List<Project> retrieveProjectsWithoutTeamsOrProjectWithGivenTeams(List<String> teamIds, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> projectCriteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> projectRoot = projectCriteriaQuery.from(Project.class);

        List<Predicate> projectWithTeamsPredicateList = Lists.newArrayList();
        projectSearchCriteriaBuilder.addPassiveIdIsNull(criteriaBuilder, projectRoot, projectWithTeamsPredicateList);
        projectSearchCriteriaBuilder.addProjectTeamIds(teamIds, criteriaBuilder, projectRoot, projectWithTeamsPredicateList);

        Predicate projectWithTeamsPredicate = criteriaBuilder.and(projectWithTeamsPredicateList.toArray(Predicate[]::new));

        List<Predicate> projectsWithoutTeamPredicateList = Lists.newArrayList();
        projectSearchCriteriaBuilder.addPassiveIdIsNull(criteriaBuilder, projectRoot, projectsWithoutTeamPredicateList);
        projectSearchCriteriaBuilder.addProjectWithoutTeamsPredicate(criteriaBuilder, projectRoot, projectsWithoutTeamPredicateList);

        Predicate projectsWithoutTeamPredicate = criteriaBuilder.and(projectsWithoutTeamPredicateList.toArray(Predicate[]::new));

        Predicate searchPredicate = criteriaBuilder.or(projectWithTeamsPredicate, projectsWithoutTeamPredicate);
        projectCriteriaQuery.where(searchPredicate);
        projectCriteriaQuery.orderBy(criteriaBuilder.asc(projectRoot.get("createdDate")));

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Project> countRoot = countQuery.from(Project.class);
        countQuery.select(criteriaBuilder.count(countRoot));

        List<Project> result = entityManager.createQuery(projectCriteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        return result;
    }
}
