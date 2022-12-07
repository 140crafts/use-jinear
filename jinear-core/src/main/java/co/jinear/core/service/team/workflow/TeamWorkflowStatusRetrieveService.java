package co.jinear.core.service.team.workflow;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.entity.team.TeamWorkflowStatus;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.repository.TeamWorkflowStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamWorkflowStatusRetrieveService {

    private final TeamWorkflowStatusRepository teamWorkflowStatusRepository;
    private final ModelMapper modelMapper;

    public TeamWorkflowStatus retrieveEntity(String teamWorkflowStatusId) {
        log.info("Retrieve team workflow status entity has started. teamWorkflowStatusId: {}", teamWorkflowStatusId);
        return teamWorkflowStatusRepository.findByTeamWorkflowStatusIdAndPassiveIdIsNull(teamWorkflowStatusId)
                .orElseThrow(NotFoundException::new);
    }

    public TeamWorkflowStatusDto retrieve(String teamWorkflowStatusId) {
        log.info("Retrieve team workflow status has started. teamWorkflowStatusId: {}", teamWorkflowStatusId);
        return teamWorkflowStatusRepository.findByTeamWorkflowStatusIdAndPassiveIdIsNull(teamWorkflowStatusId)
                .map(teamWorkflowStatus -> modelMapper.map(teamWorkflowStatus, TeamWorkflowStatusDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public List<TeamWorkflowStatusDto> retrieveAll(String teamId) {
        log.info("Retrieve all team workflow statuses has started. teamId: {}", teamId);
        return teamWorkflowStatusRepository.findAllByTeamIdAndPassiveIdIsNullOrderByOrderAsc(teamId).stream()
                .map(teamWorkflowStatus -> modelMapper.map(teamWorkflowStatus, TeamWorkflowStatusDto.class))
                .toList();
    }

    public List<TeamWorkflowStatus> retrieveAllFromSameStateGroup(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup) {
        log.info("Retrieve all from same state group team workflow statuses has started. workspaceId: {}, teamId: {}, workflowStateGroup: {}", workspaceId, teamId, workflowStateGroup);
        return teamWorkflowStatusRepository.findAllByWorkspaceIdAndTeamIdAndWorkflowStateGroupAndPassiveIdIsNullOrderByOrderAsc(workspaceId, teamId, workflowStateGroup);
    }

    public int retrieveNextAvailableOrderNo(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup) {
        return teamWorkflowStatusRepository.findFirstByWorkspaceIdAndTeamIdAndWorkflowStateGroupAndPassiveIdIsNullOrderByOrderDesc(workspaceId, teamId, workflowStateGroup)
                .map(TeamWorkflowStatus::getOrder)
                .map(order -> order + 1)
                .orElse(0);
    }

    public long teamWorkflowStatusStateGroupCount(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup) {
        return teamWorkflowStatusRepository.countAllByWorkspaceIdAndTeamIdAndWorkflowStateGroupAndPassiveIdIsNull(workspaceId, teamId, workflowStateGroup);
    }
}
