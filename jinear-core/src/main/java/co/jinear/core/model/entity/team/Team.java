package co.jinear.core.model.entity.team;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "team")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "team_id")
    private String teamId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "name")
    private String name;

    @Column(name = "tag")
    private String tag;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private TeamVisibilityType visibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_method")
    private TeamJoinMethodType joinMethod;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;

    @OneToMany(mappedBy = "team")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<TeamMember> teamMembers;

    @OneToMany(mappedBy = "team")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<TeamWorkflowStatus> workflowStatuses;
}
