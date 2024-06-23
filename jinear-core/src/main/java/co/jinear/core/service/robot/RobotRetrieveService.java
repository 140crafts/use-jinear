package co.jinear.core.service.robot;

import co.jinear.core.converter.robot.RobotDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.robot.RobotDto;
import co.jinear.core.model.entity.robot.Robot;
import co.jinear.core.repository.RobotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotRetrieveService {

    private final RobotRepository robotRepository;
    private final RobotDtoConverter robotDtoConverter;

    public Robot retrieveEntity(String robotId) {
        return robotRepository.findByRobotIdAndPassiveIdIsNull(robotId)
                .orElseThrow(NotFoundException::new);
    }

    public RobotDto retrieve(String robotId) {
        return robotRepository.findByRobotIdAndPassiveIdIsNull(robotId)
                .map(robotDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public String retrieveHashedToken(String robotId) {
        log.info("Retrieve hashed token has started. robotId: {}", robotId);
        Robot robot = retrieveEntity(robotId);
        return robot.getHashedToken();
    }
}
