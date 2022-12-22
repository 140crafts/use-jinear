package co.jinear.core.model.entity.team;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.workspace.Workspace;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "team_member")
public class TeamMember extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "team_member_id")
    private String teamMemberId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "team_id")
    private String teamId;

    @ManyToOne
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    private Team team;

}
