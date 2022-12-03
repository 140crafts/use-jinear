package co.jinear.core.model.entity.workspace;

import co.jinear.core.model.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
}
