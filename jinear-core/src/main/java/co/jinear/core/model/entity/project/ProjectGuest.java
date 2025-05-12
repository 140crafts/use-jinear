package co.jinear.core.model.entity.project;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.workspace.WorkspaceMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "project_guest")
public class ProjectGuest extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "project_guest_id")
    private String projectGuestId;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "workspace_member_id")
    private String workspaceMemberId;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "workspace_member_id", insertable = false, updatable = false)
    private WorkspaceMember workspaceMember;
}
