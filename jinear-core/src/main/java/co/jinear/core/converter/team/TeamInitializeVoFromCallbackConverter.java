package co.jinear.core.converter.team;

import co.jinear.core.model.dto.google.GoogleHandleTokenDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.enumtype.team.TeamTaskSourceType;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import co.jinear.core.model.vo.team.TeamInitializeVo;
import org.springframework.stereotype.Component;

@Component
public class TeamInitializeVoFromCallbackConverter {

    public TeamInitializeVo mapTeamInitializeVo(String currentAccountId, LocaleType localeType, String workspaceId, GoogleHandleTokenDto googleHandleTokenDto, String integrationInfoId) {
        GoogleUserInfoDto googleUserInfoDto = googleHandleTokenDto.getGoogleUserInfoDto();
        TeamInitializeVo teamInitializeVo = new TeamInitializeVo();
        teamInitializeVo.setInitializedBy(currentAccountId);
        teamInitializeVo.setWorkspaceId(workspaceId);
        teamInitializeVo.setName(googleUserInfoDto.getEmail());
        teamInitializeVo.setUsername(googleUserInfoDto.getEmail());
        teamInitializeVo.setTag(googleUserInfoDto.getEmail());
        teamInitializeVo.setVisibility(TeamVisibilityType.VISIBLE);
        teamInitializeVo.setJoinMethod(TeamJoinMethodType.FROM_TEAM_ADMIN);
        teamInitializeVo.setTaskVisibility(TeamTaskVisibilityType.VISIBLE_TO_ALL_TEAM_MEMBERS);
        teamInitializeVo.setTaskSourceType(TeamTaskSourceType.INTEGRATION);
        teamInitializeVo.setIntegrationInfoId(integrationInfoId);
        teamInitializeVo.setLocale(localeType);
        return teamInitializeVo;
    }
}
