package co.jinear.core.controller.robot;

import co.jinear.core.manager.robot.RobotManagementManager;
import co.jinear.core.model.request.robot.RobotInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.robot.RobotSecretResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/robot-management")
@RequiredArgsConstructor
public class RobotManagementController {

    private final RobotManagementManager robotManagementManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initialize(@Valid @RequestBody RobotInitializeRequest robotInitializeRequest) {
        return robotManagementManager.initializeRobot(robotInitializeRequest);
    }

    @PostMapping("/regenerate-token/{robotId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RobotSecretResponse regenerateToken(@PathVariable String robotId) {
        return robotManagementManager.regenerateToken(robotId);
    }
}
