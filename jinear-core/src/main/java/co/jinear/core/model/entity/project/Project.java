package co.jinear.core.model.entity.project;

import co.jinear.core.converter.project.ProjectPriorityTypeConverter;
import co.jinear.core.converter.project.ProjectStateTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.entity.workspace.WorkspaceMember;
import co.jinear.core.model.enumtype.project.ProjectPriorityType;
import co.jinear.core.model.enumtype.project.ProjectStateType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "project")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "project_id")
    private String projectId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "title")
    private String title;

    @Column(name = "description_rich_text_id")
    private String descriptionRichTextId;

    @Convert(converter = ProjectStateTypeConverter.class)
    @Column(name = "project_state")
    private ProjectStateType projectState;

    @Convert(converter = ProjectPriorityTypeConverter.class)
    @Column(name = "project_priority")
    private ProjectPriorityType projectPriority;

    @Column(name = "lead_workspace_member_id")
    private String leadWorkspaceMemberId;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "target_date")
    private ZonedDateTime targetDate;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "lead_workspace_member_id", referencedColumnName = "workspace_member_id", insertable = false, updatable = false)
    private WorkspaceMember leadWorkspaceMember;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "description_rich_text_id", referencedColumnName = "rich_text_id", insertable = false, updatable = false)
    private RichText description;

    @OneToMany(mappedBy = "project")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<ProjectTeam> projectTeams;

    @OneToMany(mappedBy = "project")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC, targetDate ASC, milestoneOrder ASC")
    private Set<Milestone> milestones;
}
