package co.jinear.core.repository;

import co.jinear.core.model.entity.team.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {

    Optional<TeamMember> findByAccountIdAndTeamIdAndPassiveIdIsNull(String accountId, String teamId);

    Page<TeamMember> findAllByTeamIdAndPassiveIdIsNullOrderByCreatedDateAsc(String teamId, Pageable pageable);

    List<TeamMember> findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNullAndTeam_PassiveIdIsNull(String accountId, String workspaceId);

    Long countAllByAccountIdAndTeamIdAndPassiveIdIsNull(String accountId, String teamId);
}
