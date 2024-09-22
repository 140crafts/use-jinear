package co.jinear.core.repository.project;

import co.jinear.core.model.entity.project.ProjectTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectTeamRepository extends JpaRepository<ProjectTeam, String> {

    boolean existsByProjectIdAndTeamIdAndPassiveIdIsNull(String projectId, String teamId);

    Page<ProjectTeam> findAllByTeamIdIsInAndProject_PassiveIdIsNullAndPassiveIdIsNull(@Param("teamIds") List<String> teamIds, Pageable pageable);

    List<ProjectTeam> findAllByTeamIdIsInAndProject_PassiveIdIsNullAndPassiveIdIsNull(List<String> teamIds);

    List<ProjectTeam> findAllByProjectIdAndTeamIdIsInAndPassiveIdIsNull(String projectId, List<String> teamIds);

    List<ProjectTeam> findAllByProjectIdAndTeamIdIsNotInAndPassiveIdIsNull(String projectId, List<String> teamIds);
}
