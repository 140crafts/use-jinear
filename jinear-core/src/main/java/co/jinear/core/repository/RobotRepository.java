package co.jinear.core.repository;

import co.jinear.core.model.entity.robot.Robot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RobotRepository extends JpaRepository<Robot, String> {

    Optional<Robot> findByRobotIdAndPassiveIdIsNull(String robotId);
}
