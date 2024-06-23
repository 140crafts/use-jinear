package co.jinear.core.converter.robot;

import co.jinear.core.model.dto.robot.RobotDto;
import co.jinear.core.model.dto.robot.RobotSecretDto;
import co.jinear.core.model.entity.robot.Robot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RobotDtoConverter {

    RobotDto convert(Robot robot);

    RobotSecretDto convert(Robot robot, String tokenClearText);
}
