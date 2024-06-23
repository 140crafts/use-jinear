package co.jinear.core.model.entity.robot;

import co.jinear.core.converter.robot.RobotTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.robot.RobotType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "robot")
public class Robot extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "robot_id")
    private String robotId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "robot_name")
    private String robotName;

    @Column(name = "hashed_token")
    private String hashedToken;

    @Convert(converter = RobotTypeConverter.class)
    @Column(name = "robot_type")
    private RobotType robotType;
}
