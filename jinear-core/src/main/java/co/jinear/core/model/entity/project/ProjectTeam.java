package co.jinear.core.model.entity.project;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.team.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "project_team")
public class ProjectTeam extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "project_team_id")
    private String projectTeamId;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "team_id")
    private String teamId;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    private Team team;
}
