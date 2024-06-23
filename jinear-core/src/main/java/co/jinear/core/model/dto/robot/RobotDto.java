package co.jinear.core.model.dto.robot;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.robot.RobotType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotDto extends BaseDto {

    private String robotId;
    private String workspaceId;
    private String robotName;
    private RobotType robotType;
}
