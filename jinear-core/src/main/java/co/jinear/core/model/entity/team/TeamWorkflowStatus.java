package co.jinear.core.model.entity.team;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "team_workflow_status")
public class TeamWorkflowStatus extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "team_workflow_status_id", nullable = false)
    private String teamWorkflowStatusId;

    @Column(name = "team_id", nullable = false)
    private String teamId;

    @Column(name = "workspace_id", nullable = false)
    private String workspaceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "workflow_state_group", nullable = false)
    private TeamWorkflowStateGroup workflowStateGroup;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "order", nullable = false)
    private Integer order;

    @ManyToOne
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    private Team team;
}
