package co.jinear.core.model.dto.team;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.media.MediaDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeamDto extends BaseDto {
    private String teamId;
    private String title;
    private String description;
    private String username;
    private TeamSettingDto settings;
    private MediaDto profilePicture;
}
