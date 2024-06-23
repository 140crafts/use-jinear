package co.jinear.core.model.request.robot;

import co.jinear.core.model.enumtype.robot.RobotType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class RobotInitializeRequest extends BaseRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String workspaceId;
    @Nullable
    private RobotType robotType = RobotType.MESSAGE;
}
