package co.jinear.core.repository.project;

import co.jinear.core.model.entity.project.ProjectTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectTeamRepository extends JpaRepository<ProjectTeam, String> {

    Optional<ProjectTeam> findByProjectIdAndTeamIdAndPassiveIdIsNull(String projectId, String teamId);

    boolean existsByProjectIdAndTeamIdAndPassiveIdIsNull(String projectId, String teamId);

//    @Query("""
//            select pt from ProjectTeam pt, Project p
//            where
//            pt.passiveId is null and
//            pt.project.passiveId is null and
//            (pt.teamId in :teamIds or p.projectTeams is empty )
//            """)
//    Page<ProjectTeam> findAllByTeamIdIsInAndProject_PassiveIdIsNullAndPassiveIdIsNull(@Param("teamIds") List<String> teamIds, Pageable pageable);

    Page<ProjectTeam> findAllByTeamIdIsInAndProject_PassiveIdIsNullAndPassiveIdIsNull(@Param("teamIds") List<String> teamIds, Pageable pageable);

    List<ProjectTeam> findAllByTeamIdIsInAndProject_PassiveIdIsNullAndPassiveIdIsNull(List<String> teamIds);

    List<ProjectTeam> findAllByProjectIdAndTeamIdIsInAndPassiveIdIsNull(String projectId, List<String> teamIds);

    List<ProjectTeam> findAllByProjectIdAndTeamIdIsNotInAndPassiveIdIsNull(String projectId, List<String> teamIds);
}
