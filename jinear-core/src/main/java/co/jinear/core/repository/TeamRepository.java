package co.jinear.core.repository;

import co.jinear.core.model.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, String> {

    List<Team> findAllByWorkspaceIdAndPassiveIdIsNullOrderByCreatedDateAsc(String workspaceId);

    Optional<Team> findByTeamIdAndPassiveIdIsNull(String teamId);

    Optional<Team> findByNameAndWorkspaceIdAndPassiveIdIsNull(String name, String workspaceId);

    Optional<Team> findByTagAndWorkspaceIdAndPassiveIdIsNull(String tag, String workspaceId);
}
