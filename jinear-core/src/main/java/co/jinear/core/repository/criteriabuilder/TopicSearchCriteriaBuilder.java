package co.jinear.core.repository.criteriabuilder;

import co.jinear.core.model.entity.topic.Topic;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class TopicSearchCriteriaBuilder {

    public void addWorkspaceId(String workspaceId, CriteriaBuilder criteriaBuilder, Root<Topic> root, List<Predicate> predicateList) {
        if (Objects.nonNull(workspaceId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get("workspaceId"), workspaceId);
            predicateList.add(predicate);
        }
    }

    public void addTeamId(String teamId, CriteriaBuilder criteriaBuilder, Root<Topic> root, List<Predicate> predicateList) {
        if (Objects.nonNull(teamId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get("teamId"), teamId);
            predicateList.add(predicate);
        }
    }

    public void addNameOrTag(String nameOrTag, CriteriaBuilder criteriaBuilder, Root<Topic> root, List<Predicate> predicateList) {
        if (Objects.nonNull(nameOrTag)) {
            Predicate nameLike = criteriaBuilder.like(criteriaBuilder.upper(root.<String>get("name")), "%" + nameOrTag.toUpperCase() + "%");
            Predicate tagLike = criteriaBuilder.like(criteriaBuilder.upper(root.<String>get("tag")), "%" + nameOrTag.toUpperCase() + "%");
            Predicate nameLikeOrTagLike = criteriaBuilder.or(nameLike, tagLike);
            predicateList.add(nameLikeOrTagLike);
        }
    }
}
