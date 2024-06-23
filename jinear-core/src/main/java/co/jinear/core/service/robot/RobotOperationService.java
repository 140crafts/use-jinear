package co.jinear.core.service.robot;

import co.jinear.core.converter.robot.RobotDtoConverter;
import co.jinear.core.model.dto.robot.RobotSecretDto;
import co.jinear.core.model.entity.robot.Robot;
import co.jinear.core.model.enumtype.robot.RobotType;
import co.jinear.core.repository.RobotRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.JwtHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotOperationService {

    private final RobotRepository robotRepository;
    private final RobotRetrieveService robotRetrieveService;
    private final RobotDtoConverter robotDtoConverter;
    private final PassiveService passiveService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtHelper jwtHelper;

    public RobotSecretDto initializeRobot(String name, String workspaceId, RobotType robotType) {
        log.info("Initialize robot has started. workspaceId: {}, robotType: {}, name: {}", workspaceId, robotType, name);
        Robot robot = mapEntity(name, workspaceId, robotType);
        Robot saved = robotRepository.save(robot);
        String token = generateAndUpdateToken(saved);
        return robotDtoConverter.convert(saved, token);
    }

    public RobotSecretDto resetToken(String robotId) {
        log.info("Reset token has started. robotId: {}", robotId);
        Robot robot = robotRetrieveService.retrieveEntity(robotId);
        String token = generateAndUpdateToken(robot);
        return robotDtoConverter.convert(robot, token);
    }

    public String deleteRobot(String robotId) {
        log.info("Delete robot has started. robotId: {}", robotId);
        Robot robot = robotRetrieveService.retrieveEntity(robotId);
        String passiveId = passiveService.createUserActionPassive();
        robot.setPassiveId(passiveId);
        robotRepository.save(robot);
        log.info("Delete robot has completed. robotId: {}, passiveId: {}", robotId, passiveId);
        return passiveId;
    }

    private String generateAndUpdateToken(Robot saved) {
        log.info("Generate and update token has started.");
        String token = jwtHelper.generateRobotToken(saved.getRobotId());
        saved.setHashedToken(bCryptPasswordEncoder.encode(token));
        robotRepository.save(saved);
        return token;
    }

    private Robot mapEntity(String name, String workspaceId, RobotType robotType) {
        Robot robot = new Robot();
        robot.setRobotType(robotType);
        robot.setWorkspaceId(workspaceId);
        robot.setRobotName(name);
        return robot;
    }
}
