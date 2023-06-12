package co.jinear.core.repository;

import co.jinear.core.model.entity.topic.Topic;
import co.jinear.core.repository.criteriabuilder.TopicSearchCriteriaBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TopicSearchRepository {

    private static final int PAGE_SIZE = 250;

    private final EntityManager entityManager;
    private final TopicSearchCriteriaBuilder topicSearchCriteriaBuilder;

    public List<Topic> filterBy(String workspaceId, String teamId, String nameOrTag) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Topic> topicCriteriaQuery = criteriaBuilder.createQuery(Topic.class);
        Root<Topic> topicRoot = topicCriteriaQuery.from(Topic.class);

        List<Predicate> predicateList = retrieveSearchPredicateList(workspaceId, teamId, nameOrTag, criteriaBuilder, topicRoot);

        topicCriteriaQuery
                .where(predicateList.toArray(Predicate[]::new))
                .orderBy(criteriaBuilder.desc(topicRoot.get("createdDate")));

        return entityManager.createQuery(topicCriteriaQuery)
                .setFirstResult(0)
                .setMaxResults(PAGE_SIZE)
                .getResultList();
    }

    private List<Predicate> retrieveSearchPredicateList(String workspaceId, String teamId, String nameOrTag, CriteriaBuilder criteriaBuilder, Root<Topic> topicRoot) {
        List<Predicate> predicateList = new ArrayList<>();
        topicSearchCriteriaBuilder.addWorkspaceId(workspaceId, criteriaBuilder, topicRoot, predicateList);
        topicSearchCriteriaBuilder.addTeamId(teamId, criteriaBuilder, topicRoot, predicateList);
        topicSearchCriteriaBuilder.addNameOrTag(nameOrTag, criteriaBuilder, topicRoot, predicateList);
        return predicateList;
    }
}
