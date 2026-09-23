package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "task_collaborator")
public class TaskCollaborator extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "task_collaborator_id")
    private String taskCollaboratorId;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "account_id")
    private String accountId;

    @ManyToOne
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account collaborator;
}
