package co.jinear.core.service.team.workflow;

import co.jinear.core.converter.team.TeamWorkflowStatusConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.team.workflow.GroupedTeamWorkflowStatusListDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.entity.team.TeamWorkflowStatus;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.repository.TeamWorkflowStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamWorkflowStatusRetrieveService {

    private final TeamWorkflowStatusRepository teamWorkflowStatusRepository;
    private final TeamWorkflowStatusConverter teamWorkflowStatusConverter;

    public TeamWorkflowStatus retrieveEntity(String teamWorkflowStatusId) {
        log.info("Retrieve team workflow status entity has started. teamWorkflowStatusId: {}", teamWorkflowStatusId);
        return teamWorkflowStatusRepository.findByTeamWorkflowStatusIdAndPassiveIdIsNull(teamWorkflowStatusId)
                .orElseThrow(NotFoundException::new);
    }

    public TeamWorkflowStatusDto retrieve(String teamWorkflowStatusId) {
        log.info("Retrieve team workflow status has started. teamWorkflowStatusId: {}", teamWorkflowStatusId);
        return retrieveOptional(teamWorkflowStatusId)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<TeamWorkflowStatusDto> retrieveOptional(String teamWorkflowStatusId) {
        log.info("Retrieve team workflow status has started. teamWorkflowStatusId: {}", teamWorkflowStatusId);
        return teamWorkflowStatusRepository.findByTeamWorkflowStatusIdAndPassiveIdIsNull(teamWorkflowStatusId)
                .map(teamWorkflowStatusConverter::map);
    }

    public List<TeamWorkflowStatusDto> retrieveAll(String teamId) {
        log.info("Retrieve all team workflow statuses has started. teamId: {}", teamId);
        return teamWorkflowStatusRepository.findAllByTeamIdAndPassiveIdIsNullOrderByOrderAsc(teamId).stream()
                .map(teamWorkflowStatusConverter::map)
                .toList();
    }

    public GroupedTeamWorkflowStatusListDto retrieveAllGrouped(String teamId) {
        log.info("Retrieve all team workflow grouped statuses has started. teamId: {}", teamId);
        List<TeamWorkflowStatusDto> teamWorkflowStatusDtos = retrieveAll(teamId);
        HashMap<TeamWorkflowStateGroup, List<TeamWorkflowStatusDto>> groupedTeamWorkflowStatuses = new HashMap<>();
        teamWorkflowStatusDtos.stream().forEach(teamWorkflowStatusDto -> {
            List<TeamWorkflowStatusDto> list = groupedTeamWorkflowStatuses.getOrDefault(teamWorkflowStatusDto.getWorkflowStateGroup(), new ArrayList<>());
            list.add(teamWorkflowStatusDto);
            groupedTeamWorkflowStatuses.put(teamWorkflowStatusDto.getWorkflowStateGroup(), list);
        });
        GroupedTeamWorkflowStatusListDto groupedTeamWorkflowStatusListDto = new GroupedTeamWorkflowStatusListDto();
        groupedTeamWorkflowStatusListDto.setGroupedTeamWorkflowStatuses(groupedTeamWorkflowStatuses);
        return groupedTeamWorkflowStatusListDto;
    }

    public List<TeamWorkflowStatus> retrieveAllFromSameStateGroup(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup) {
        log.info("Retrieve all from same state group team workflow statuses has started. workspaceId: {}, teamId: {}, workflowStateGroup: {}", workspaceId, teamId, workflowStateGroup);
        return teamWorkflowStatusRepository.findAllByWorkspaceIdAndTeamIdAndWorkflowStateGroupAndPassiveIdIsNullOrderByOrderAsc(workspaceId, teamId, workflowStateGroup);
    }

    public TeamWorkflowStatusDto retrieveFirstFromGroup(String teamId, TeamWorkflowStateGroup workflowStateGroup) {
        log.info("Retrieve first from group has started. teamId: {}, workflowStateGroup: {}", teamId, workflowStateGroup);
        return teamWorkflowStatusRepository.findFirstByTeamIdAndWorkflowStateGroupAndPassiveIdIsNullOrderByOrderDesc(teamId, workflowStateGroup)
                .map(teamWorkflowStatusConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public int retrieveNextAvailableOrderNo(String teamId, TeamWorkflowStateGroup workflowStateGroup) {
        return teamWorkflowStatusRepository.findFirstByTeamIdAndWorkflowStateGroupAndPassiveIdIsNullOrderByOrderDesc(teamId, workflowStateGroup)
                .map(TeamWorkflowStatus::getOrder)
                .map(order -> order + 1)
                .orElse(0);
    }

    public long teamWorkflowStatusStateGroupCount(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup) {
        return teamWorkflowStatusRepository.countAllByWorkspaceIdAndTeamIdAndWorkflowStateGroupAndPassiveIdIsNull(workspaceId, teamId, workflowStateGroup);
    }
}
