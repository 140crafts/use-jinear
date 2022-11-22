package co.jinear.core.repository;

import co.jinear.core.model.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, String> {

    List<Team> findAllByTeamIdIsInAndPassiveIdIsNull(List<String> teamIds);

    Optional<Team> findByUsername_UsernameAndUsername_PassiveIdIsNullAndPassiveIdIsNull(String username);

    Optional<Team> findByTeamIdAndPassiveIdIsNull(String teamId);
}
