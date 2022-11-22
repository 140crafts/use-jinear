package co.jinear.core.model.entity.team;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.team.TeamAccountRoleType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "account_id")
    private String accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private TeamAccountRoleType role;
}
