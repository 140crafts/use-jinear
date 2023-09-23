package co.jinear.core.model.entity.workspace;

import co.jinear.core.converter.workspace.WorkspaceTierTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.team.Team;
import co.jinear.core.model.entity.username.Username;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "workspace")
public class Workspace extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
    
    @Convert(converter = WorkspaceTierTypeConverter.class)
    @Column(name = "tier")
    private WorkspaceTier tier;

    @OneToOne(mappedBy = "workspace")
    private Username username;

    @OneToOne(mappedBy = "workspace")
    private WorkspaceSetting settings;

    @OneToMany(mappedBy = "workspace")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<Team> teams;
}