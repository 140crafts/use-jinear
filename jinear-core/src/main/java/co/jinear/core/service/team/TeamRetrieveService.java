package co.jinear.core.service.team;

import co.jinear.core.converter.integration.IntegrationInfoDtoConverter;
import co.jinear.core.converter.team.TeamConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.entity.integration.IntegrationInfo;
import co.jinear.core.model.entity.team.Team;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.repository.TeamRepository;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamRetrieveService {

    private final TeamRepository teamRepository;
    private final WorkspaceMemberRetrieveService workspaceMemberRetrieveService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TeamConverter teamConverter;
    private final IntegrationInfoDtoConverter integrationInfoDtoConverter;

    public List<TeamDto> listWorkspaceTeamsByAccountWorkspaceRole(String accountId, String workspaceId) {
        log.info("List workspace teams by account role has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        WorkspaceAccountRoleType workspaceAccountRoleType = workspaceMemberRetrieveService.retrieveAccountWorkspaceRole(accountId, workspaceId);
        log.info("Account's workspace role is: {}", workspaceAccountRoleType);
        if (Arrays.asList(OWNER, ADMIN).contains(workspaceAccountRoleType)) {
            return retrieveWorkspaceTeams(workspaceId);
        } else if (MEMBER.equals(workspaceAccountRoleType)) {
            return retrieveAccountWorkspaceTeams(accountId, workspaceId);
        }
        throw new BusinessException();
    }

    public List<TeamDto> retrieveWorkspaceTeams(String workspaceId) {
        log.info("Retrieve workspace teams has started. workspaceId: {}", workspaceId);
        return teamRepository.findAllByWorkspaceIdAndPassiveIdIsNullOrderByCreatedDateAsc(workspaceId).stream()
                .map(teamConverter::map)
                .toList();
    }

    public List<TeamDto> retrieveAccountWorkspaceTeams(String accountId, String workspaceId) {
        log.info("Retrieve account workspace teams has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        return teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(accountId, workspaceId).stream()
                .map(TeamMemberDto::getTeam)
                .toList();
    }

    public Team retrieveEntity(String teamId) {
        log.info("Retrieve team entity has started. teamId: {}", teamId);
        return teamRepository.findByTeamIdAndPassiveIdIsNull(teamId)
                .orElseThrow(NotFoundException::new);
    }

    public TeamDto retrieveTeam(String teamId) {
        log.info("Retrieve team has started. teamId: {}", teamId);
        return teamRepository.findByTeamIdAndPassiveIdIsNull(teamId)
                .map(teamConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public TeamDto retrieveActiveTeamByUsername(String teamUsername, String workspaceId) {
        log.info("Retrieve active team by name has started. teamUsername: {}, workspaceId: {}", teamUsername, workspaceId);
        return teamRepository.findByUsernameAndWorkspaceIdAndPassiveIdIsNull(teamUsername, workspaceId)
                .map(teamConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<TeamDto> retrieveTeamIncludingPassivesByNameOptional(String teamName, String workspaceId) {
        log.info("Retrieve team including passives by name has started. teamName: {}, workspaceId: {}", teamName, workspaceId);
        return teamRepository.findByNameAndWorkspaceId(teamName, workspaceId)
                .map(teamConverter::map);
    }

    public TeamDto retrieveTeamByTag(String tag, String workspaceId) {
        log.info("Retrieve team by tag has started. tag: {}, workspaceId: {}", tag, workspaceId);
        return retrieveTeamByTagOptional(tag, workspaceId)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<TeamDto> retrieveTeamByTagOptional(String tag, String workspaceId) {
        log.info("Retrieve team by tag has started. tag: {}, workspaceId: {}", tag, workspaceId);
        return teamRepository.findByTagAndWorkspaceIdAndPassiveIdIsNull(tag, workspaceId)
                .map(teamConverter::map);
    }

    public Optional<TeamDto> retrieveTeamByUsernameOptional(String username, String workspaceId) {
        log.info("Retrieve team by username has started. username: {}, workspaceId: {}", username, workspaceId);
        return teamRepository.findByUsernameAndWorkspaceIdAndPassiveIdIsNull(username, workspaceId)
                .map(teamConverter::map);
    }

    public IntegrationInfoDto retrieveTeamIntegrationInfo(String teamId) {
        IntegrationInfo integrationInfo = retrieveEntity(teamId).getIntegrationInfo();
        return integrationInfoDtoConverter.map(integrationInfo);
    }
}
