package co.jinear.core.model.entity.username;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.model.enumtype.username.UsernameRelatedObjectType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "username")
public class Username extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "username_id")
    private String usernameId;

    @Column(name = "related_object_id")
    private String relatedObjectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "related_object_type")
    private UsernameRelatedObjectType relatedObjectType;

    @Column(name = "username")
    private String username;

    @ToString.Exclude
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "related_object_id", insertable = false, updatable = false)
    private Account account;

    @ToString.Exclude
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "related_object_id", insertable = false, updatable = false)
    private Workspace workspace;
}
