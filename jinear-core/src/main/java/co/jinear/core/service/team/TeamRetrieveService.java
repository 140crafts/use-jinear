package co.jinear.core.service.team;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.TeamMemberDto;
import co.jinear.core.repository.TeamRepository;
import co.jinear.core.service.team.member.TeamMemberListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamRetrieveService {

    private final TeamRepository teamRepository;
    private final TeamMemberListingService teamMemberListingService;
    private final ModelMapper modelMapper;

    public TeamDto retrieveTeamWithId(String teamId) {
        log.info("Retrieving team with id: {}", teamId);
        return teamRepository.findByTeamIdAndPassiveIdIsNull(teamId)
                .map(team -> modelMapper.map(team, TeamDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public TeamDto retrieveTeamWithUsername(String teamUsername) {
        log.info("Retrieving team with username: {}", teamUsername);
        return teamRepository.findByUsername_UsernameAndUsername_PassiveIdIsNullAndPassiveIdIsNull(teamUsername)
                .map(team -> modelMapper.map(team, TeamDto.class))
                .orElseThrow(NotFoundException::new);
    }

    public List<TeamDto> retrieveAllById(List<String> teamIds) {
        log.info("Retrieve all by team id has started. teamIds", StringUtils.join(teamIds, ", "));
        return teamRepository.findAllByTeamIdIsInAndPassiveIdIsNull(teamIds)
                .stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .toList();
    }

    public List<TeamDto> retrieveAccountTeams(String accountId){
        List<String> accountTeamIds= teamMemberListingService.retrieveTeamMembersByAccountId(accountId)
                .stream()
                .map(TeamMemberDto::getTeamId)
                .toList();
        return retrieveAllById(accountTeamIds);
    }
}
