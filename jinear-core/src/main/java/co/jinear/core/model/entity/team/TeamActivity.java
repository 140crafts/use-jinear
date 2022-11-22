package co.jinear.core.model.entity.team;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.team.TeamActivityType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "team_activity")
public class TeamActivity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "team_activity_id")
    private String teamActivityId;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "related_object_id")
    private String relatedObjectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type")
    private TeamActivityType type;
}
