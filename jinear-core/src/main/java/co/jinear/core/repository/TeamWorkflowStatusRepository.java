package co.jinear.core.repository;

import co.jinear.core.model.entity.team.TeamWorkflowStatus;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamWorkflowStatusRepository extends JpaRepository<TeamWorkflowStatus, String> {

    Optional<TeamWorkflowStatus> findByTeamWorkflowStatusIdAndPassiveIdIsNull(String teamWorkflowStatusId);

    List<TeamWorkflowStatus> findAllByTeamIdAndPassiveIdIsNullOrderByOrderAsc( String teamId);

    List<TeamWorkflowStatus> findAllByWorkspaceIdAndTeamIdAndWorkflowStateGroupAndPassiveIdIsNullOrderByOrderAsc(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup);

    Optional<TeamWorkflowStatus> findFirstByTeamIdAndWorkflowStateGroupAndPassiveIdIsNullOrderByOrderDesc(String teamId, TeamWorkflowStateGroup workflowStateGroup);

    Long countAllByWorkspaceIdAndTeamIdAndWorkflowStateGroupAndPassiveIdIsNull(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup);
}
