package co.jinear.core.service.team;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.TeamSettingDto;
import co.jinear.core.model.dto.username.UsernameDto;
import co.jinear.core.model.entity.team.Team;
import co.jinear.core.model.enumtype.team.TeamAccountRoleType;
import co.jinear.core.model.enumtype.username.UsernameRelatedObjectType;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import co.jinear.core.model.vo.team.TeamSettingsInitializeVo;
import co.jinear.core.model.vo.team.InitializeTeamMemberVo;
import co.jinear.core.model.vo.username.InitializeUsernameVo;
import co.jinear.core.repository.TeamRepository;
import co.jinear.core.service.team.member.TeamMemberService;
import co.jinear.core.service.username.UsernameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamInitializeService {

    private final TeamRepository teamRepository;
    private final UsernameService usernameService;
    private final TeamSettingService teamSettingService;
    private final TeamMemberService teamMemberService;

    @Transactional
    public TeamDto initializeTeam(TeamInitializeVo teamInitializeVo) {
        log.info("Initialize team has started. teamInitializeVo: {}", teamInitializeVo);
        Team team = saveTeam(teamInitializeVo);
        UsernameDto usernameDto = assignUsername(teamInitializeVo, team);
        TeamSettingDto teamSettingDto = initializeSettings(teamInitializeVo, team);
        assignOwner(teamInitializeVo, team);
        return mapValues(team, usernameDto, teamSettingDto);
    }

    private void assignOwner(TeamInitializeVo teamInitializeVo, Team team) {
        InitializeTeamMemberVo initializeTeamMemberVo = new InitializeTeamMemberVo();
        initializeTeamMemberVo.setAccountId(teamInitializeVo.getOwnerId());
        initializeTeamMemberVo.setTeamId(team.getTeamId());
        initializeTeamMemberVo.setRole(TeamAccountRoleType.OWNER);
        teamMemberService.initializeTeamMember(initializeTeamMemberVo);
    }

    private TeamSettingDto initializeSettings(TeamInitializeVo teamInitializeVo, Team team) {
        TeamSettingsInitializeVo teamSettingsInitializeVo = new TeamSettingsInitializeVo();
        teamSettingsInitializeVo.setTeamId(team.getTeamId());
        teamSettingsInitializeVo.setVisibility(teamInitializeVo.getVisibility());
        teamSettingsInitializeVo.setJoinType(teamInitializeVo.getJoinType());
        return teamSettingService.initializeTeamSettings(teamSettingsInitializeVo);
    }

    private UsernameDto assignUsername(TeamInitializeVo teamInitializeVo, Team team) {
        Boolean appendRandomStrOnCollision = Objects.isNull(teamInitializeVo.getAppendRandomStrOnCollision()) ?
                Boolean.FALSE : teamInitializeVo.getAppendRandomStrOnCollision();
        InitializeUsernameVo initializeUsernameVo = new InitializeUsernameVo();
        initializeUsernameVo.setRelatedObjectId(team.getTeamId());
        initializeUsernameVo.setRelatedObjectType(UsernameRelatedObjectType.TEAMS);
        initializeUsernameVo.setUsername(teamInitializeVo.getHandle());
        initializeUsernameVo.setAppendRandomStrOnCollision(appendRandomStrOnCollision);
        return usernameService.assignUsername(initializeUsernameVo);
    }

    private Team saveTeam(TeamInitializeVo teamInitializeVo) {
        Team team = new Team();
        team.setTitle(teamInitializeVo.getTitle());
        team.setDescription(teamInitializeVo.getDescription());
        return teamRepository.save(team);
    }

    private TeamDto mapValues(Team team, UsernameDto usernameDto, TeamSettingDto teamSettingDto) {
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamId(team.getTeamId());
        teamDto.setTitle(team.getTitle());
        teamDto.setDescription(team.getDescription());
        teamDto.setUsername(usernameDto.getUsername());
        teamDto.setSettings(teamSettingDto);
        return teamDto;
    }
}
