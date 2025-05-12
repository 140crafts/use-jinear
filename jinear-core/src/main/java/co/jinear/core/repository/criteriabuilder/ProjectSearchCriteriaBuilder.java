package co.jinear.core.repository.criteriabuilder;

import co.jinear.core.model.entity.project.Project;
import co.jinear.core.model.entity.project.ProjectTeam;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ProjectSearchCriteriaBuilder {

    public void addPassiveIdIsNull(CriteriaBuilder criteriaBuilder, Root<Project> root, List<Predicate> predicateList) {
        Predicate predicate = criteriaBuilder.isNull(root.<String>get("passiveId"));
        predicateList.add(predicate);
    }

    public void addProjectTeamIds(List<String> teamIdList, CriteriaBuilder criteriaBuilder, Root<Project> root, List<Predicate> predicateList) {
        if (Objects.nonNull(teamIdList) && !teamIdList.isEmpty()) {
            Join<Project, ProjectTeam> join = root.join("projectTeams", JoinType.LEFT);
            Expression<String> expression = join.get("teamId").as(String.class);

            CriteriaBuilder.In<String> in = criteriaBuilder.in(expression);
            teamIdList.forEach(in::value);
            criteriaBuilder.asc(join.get("createdDate"));
            predicateList.add(in);


            Expression<String> projectTeamPassiveIdNullExpression = join.get("passiveId").as(String.class);
            Predicate passiveIdIsNull = criteriaBuilder.isNull(projectTeamPassiveIdNullExpression);
            predicateList.add(passiveIdIsNull);
        }
    }

    public void addProjectWithoutTeamsPredicate(CriteriaBuilder criteriaBuilder, Root<Project> root, List<Predicate> predicateList) {
        Join<Project, ProjectTeam> join = root.join("projectTeams", JoinType.LEFT);
        Expression<String> expressionTeamId = join.get("teamId").as(String.class);
        Expression<String> expressionPassiveId = join.get("passiveId").as(String.class);

        Predicate predicateTeamId = criteriaBuilder.isNull(expressionTeamId);
        Predicate predicatePassiveId = criteriaBuilder.isNull(expressionPassiveId);
        Predicate predicate = criteriaBuilder.and(predicateTeamId, predicatePassiveId);

        predicateList.add(predicate);
    }
}
