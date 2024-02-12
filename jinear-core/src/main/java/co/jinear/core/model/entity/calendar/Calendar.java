package co.jinear.core.model.entity.calendar;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.integration.IntegrationInfo;
import co.jinear.core.model.entity.workspace.Workspace;
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
@Table(name = "calendar")
public class Calendar  extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "calendar_id")
    private String calendarId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "initialized_by")
    private String initializedBy;

    @Column(name = "integration_info_id")
    private String integrationInfoId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "integration_info_id", insertable = false, updatable = false)
    private IntegrationInfo integrationInfo;

    @OneToMany(mappedBy = "calendar")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<CalendarMember> calendarMembers;
}
