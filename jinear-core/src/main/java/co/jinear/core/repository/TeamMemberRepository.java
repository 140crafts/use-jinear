package co.jinear.core.repository;

import co.jinear.core.model.entity.team.TeamMember;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {

    Optional<TeamMember> findByTeamMemberIdAndPassiveIdIsNull(String teamMemberId);

    Optional<TeamMember> findByAccountIdAndTeamIdAndPassiveIdIsNull(String accountId, String teamId);

    Optional<TeamMember> findByAccountIdAndWorkspaceIdAndTeamIdAndPassiveIdIsNull(String accountId, String workspaceId, String teamId);

    Page<TeamMember> findAllByTeamIdAndPassiveIdIsNullOrderByCreatedDateAsc(String teamId, Pageable pageable);

    List<TeamMember> findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNullAndTeam_PassiveIdIsNullOrderByCreatedDateAsc(String accountId, String workspaceId);

    Long countAllByAccountIdAndTeamIdAndPassiveIdIsNull(String accountId, String teamId);

    Long countAllByAccountIdAndTeamIdAndRoleAndPassiveIdIsNull(String accountId, String teamId, TeamMemberRoleType role);

    List<TeamMember> findAllByWorkspaceIdAndAccountIdAndTeamIdInAndPassiveIdIsNull(String workspaceId, String accountId, List<String> teamId);
}
