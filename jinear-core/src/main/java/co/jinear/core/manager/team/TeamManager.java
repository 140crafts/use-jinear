package co.jinear.core.manager.team;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.enumtype.team.TeamActivityType;
import co.jinear.core.model.request.team.TeamInitializeRequest;
import co.jinear.core.model.response.team.TeamBaseResponse;
import co.jinear.core.model.vo.team.TeamActivityCreateVo;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.team.TeamActivityService;
import co.jinear.core.service.team.TeamInitializeService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.validator.team.TeamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamManager {

    private final TeamInitializeService teamInitializeService;
    private final TeamActivityService teamActivityService;
    private final TeamValidator teamValidator;
    private final TeamRetrieveService teamRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final MediaRetrieveService mediaRetrieveService;
    private final ModelMapper modelMapper;

    public TeamBaseResponse retrieveTeamWithUsername(String teamUsername) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        log.info("Retrieve team by username has started. teamUsername: {}, currentAccountId: {}", teamUsername, currentAccountId);
        TeamDto teamDto = teamRetrieveService.retrieveTeamWithUsername(teamUsername);
        teamValidator.validateHasAccess(currentAccountId, teamDto);
        mediaRetrieveService.retrieveProfilePictureOptional(teamDto.getTeamId())
                .ifPresent(teamDto::setProfilePicture);
        return mapValues(teamDto);
    }

    public TeamBaseResponse retrieveTeamWithId(String teamId) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        log.info("Retrieve team by id has started. teamId: {}, currentAccountId: {}", teamId, currentAccountId);
        TeamDto teamDto = teamRetrieveService.retrieveTeamWithId(teamId);
        teamValidator.validateHasAccess(currentAccountId, teamDto);
        mediaRetrieveService.retrieveProfilePictureOptional(teamDto.getTeamId())
                .ifPresent(teamDto::setProfilePicture);
        return mapValues(teamDto);
    }

    public TeamBaseResponse initializeTeam(TeamInitializeRequest teamInitializeRequest) {
        log.info("Initialize team has started with request: {}", teamInitializeRequest);
        String accountId = sessionInfoService.currentAccountId();
        TeamDto teamDto = initializeTeam(teamInitializeRequest, accountId);
        createTeamActivity(accountId, teamDto);
        return mapValues(teamDto);
    }

    private TeamDto initializeTeam(TeamInitializeRequest teamInitializeRequest, String accountId) {
        TeamInitializeVo teamInitializeVo = modelMapper.map(teamInitializeRequest, TeamInitializeVo.class);
        teamInitializeVo.setOwnerId(accountId);
        return teamInitializeService.initializeTeam(teamInitializeVo);
    }

    private void createTeamActivity(String accountId, TeamDto teamDto) {
        TeamActivityCreateVo teamActivityCreateVo = new TeamActivityCreateVo();
        teamActivityCreateVo.setTeamId(teamDto.getTeamId());
        teamActivityCreateVo.setAccountId(accountId);
        teamActivityCreateVo.setRelatedObjectId(accountId);
        teamActivityCreateVo.setType(TeamActivityType.JOIN);
        teamActivityService.createTeamActivity(teamActivityCreateVo);
    }


    private TeamBaseResponse mapValues(TeamDto teamDto) {
        TeamBaseResponse teamResponse = new TeamBaseResponse();
        teamResponse.setTeam(teamDto);
        log.info("Initialize team has ended. teamResponse: {}", teamResponse);
        return teamResponse;
    }
}
