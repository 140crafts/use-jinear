package co.jinear.core.model.dto.robot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotSecretDto extends RobotDto {

    private String tokenClearText;
}
