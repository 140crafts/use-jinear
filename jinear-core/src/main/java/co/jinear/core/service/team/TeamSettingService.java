package co.jinear.core.service.team;

import co.jinear.core.model.dto.team.TeamSettingDto;
import co.jinear.core.model.entity.team.TeamSetting;
import co.jinear.core.model.vo.team.TeamSettingsInitializeVo;
import co.jinear.core.repository.TeamSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamSettingService {

    private final TeamSettingRepository teamSettingRepository;
    private final ModelMapper modelMapper;

    public TeamSettingDto initializeTeamSettings(TeamSettingsInitializeVo teamSettingsInitializeVo) {
        log.info("Initialize team settings has started. teamSettingsInitializeVo: {}", teamSettingsInitializeVo);
        TeamSetting teamSetting = initialize(teamSettingsInitializeVo);
        return modelMapper.map(teamSetting, TeamSettingDto.class);
    }

    private TeamSetting initialize(TeamSettingsInitializeVo teamSettingsInitializeVo) {
        TeamSetting teamSetting = new TeamSetting();
        teamSetting.setTeamId(teamSettingsInitializeVo.getTeamId());
        teamSetting.setVisibility(teamSettingsInitializeVo.getVisibility());
        teamSetting.setJoinType(teamSettingsInitializeVo.getJoinType());
        return teamSettingRepository.save(teamSetting);
    }
}
