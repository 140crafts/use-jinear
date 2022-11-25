package co.jinear.core.model.entity.workspace;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.username.Username;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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

    @Column(name = "is_personal")
    private Boolean isPersonal;

    @OneToOne(mappedBy = "workspace")
    private Username username;

    @OneToOne(mappedBy = "workspace")
    private WorkspaceSetting settings;
}