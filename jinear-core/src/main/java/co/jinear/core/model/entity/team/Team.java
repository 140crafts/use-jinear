package co.jinear.core.model.entity.team;

import co.jinear.core.converter.workspace.TeamTaskSourceTypeConverter;
import co.jinear.core.converter.workspace.TeamTaskVisibilityTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.integration.IntegrationInfo;
import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.model.enumtype.team.TeamJoinMethodType;
import co.jinear.core.model.enumtype.team.TeamTaskSourceType;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

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

    @Column(name = "initialized_by")
    private String initializedBy;

    @Column(name = "name")
    private String name;

    @Column(name = "tag")
    private String tag;

    @Column(name = "username")
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private TeamVisibilityType visibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_method")
    private TeamJoinMethodType joinMethod;

    @Convert(converter = TeamTaskVisibilityTypeConverter.class)
    @Column(name = "task_visibility")
    private TeamTaskVisibilityType taskVisibility;

    @Convert(converter = TeamTaskSourceTypeConverter.class)
    @Column(name = "task_source_type")
    private TeamTaskSourceType taskSourceType;

    @Column(name = "integration_info_id")
    private String integrationInfoId;

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

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "integration_info_id", insertable = false, updatable = false)
    private IntegrationInfo integrationInfo;
}
