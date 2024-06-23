package co.jinear.core.manager.robot;

import co.jinear.core.model.dto.robot.RobotDto;
import co.jinear.core.model.dto.robot.RobotSecretDto;
import co.jinear.core.model.request.robot.RobotInitializeRequest;
import co.jinear.core.model.response.robot.RobotSecretResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.robot.RobotOperationService;
import co.jinear.core.service.robot.RobotRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotManagementManager {

    private final RobotOperationService robotOperationService;
    private final RobotRetrieveService robotRetrieveService;
    private final WorkspaceValidator workspaceValidator;
    private final SessionInfoService sessionInfoService;

    public RobotSecretResponse initializeRobot(RobotInitializeRequest robotInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.isWorkspaceAdminOrOwner(currentAccountId, robotInitializeRequest.getWorkspaceId());
        log.info("Initialize robot has started. currentAccountId: {}", currentAccountId);
        RobotSecretDto robotSecretDto = robotOperationService.initializeRobot(robotInitializeRequest.getName(), robotInitializeRequest.getWorkspaceId(), robotInitializeRequest.getRobotType());
        return mapResponse(robotSecretDto);
    }

    public RobotSecretResponse regenerateToken(String robotId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        RobotDto robotDto = robotRetrieveService.retrieve(robotId);
        workspaceValidator.isWorkspaceAdminOrOwner(currentAccountId, robotDto.getWorkspaceId());
        log.info("Initialize robot has started. currentAccountId: {}", currentAccountId);
        RobotSecretDto robotSecretDto = robotOperationService.resetToken(robotId);
        return mapResponse(robotSecretDto);
    }

    private RobotSecretResponse mapResponse(RobotSecretDto robotSecretDto) {
        RobotSecretResponse robotSecretResponse = new RobotSecretResponse();
        robotSecretResponse.setRobotSecretDto(robotSecretDto);
        return robotSecretResponse;
    }
}
