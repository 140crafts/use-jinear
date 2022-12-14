package co.jinear.core.model.entity.workspace;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.team.Team;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "workspace_display_preference")
public class WorkspaceDisplayPreference extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "workspace_display_preference_id")
    private String workspaceDisplayPreferenceId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "preferred_workspace_id")
    private String preferredWorkspaceId;

    @Column(name = "preferred_team_id")
    private String preferredTeamId;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "preferred_workspace_id", insertable = false, updatable = false)
    private Workspace workspace;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "preferred_team_id", insertable = false, updatable = false)
    private Team team;
}
