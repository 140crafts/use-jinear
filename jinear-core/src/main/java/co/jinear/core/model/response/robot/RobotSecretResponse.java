package co.jinear.core.model.response.robot;

import co.jinear.core.model.dto.robot.RobotSecretDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotSecretResponse extends BaseResponse {

    @JsonProperty("data")
    private RobotSecretDto robotSecretDto;
}
