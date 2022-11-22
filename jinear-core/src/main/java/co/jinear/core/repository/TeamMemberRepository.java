package co.jinear.core.repository;

import co.jinear.core.model.entity.team.TeamMember;
import co.jinear.core.model.enumtype.team.TeamAccountRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {

    Long countAllByAccountIdAndTeamIdAndRoleAndPassiveIdIsNull(String accountId, String teamId, TeamAccountRoleType role);

    Long countAllByAccountIdAndTeamIdAndPassiveIdIsNull(String accountId, String teamId);

    Long countAllByAccountIdAndTeamIdAndRoleIsInAndPassiveIdIsNull(String accountId, String teamId, List<TeamAccountRoleType> roleTypes);

    Page<TeamMember> findAllByTeamIdAndPassiveIdIsNull(String teamId, Pageable pageable);

    List<TeamMember> findAllByAccountIdAndPassiveIdIsNull(String accountId);

    Optional<TeamMember> findByAccountIdAndTeamIdAndPassiveIdIsNull(String accountId, String teamId);
}
