package co.jinear.core.model.entity.team;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.team.TeamContentVisibilityType;
import co.jinear.core.model.enumtype.team.TeamJoinType;
import co.jinear.core.model.enumtype.team.TeamVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "team_setting")
public class TeamSetting extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "team_setting_id")
    private String teamSettingId;

    @Column(name = "team_id", unique = true)
    private String teamId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private TeamVisibilityType visibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_visibility")
    private TeamContentVisibilityType contentVisibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_type")
    private TeamJoinType joinType;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    private Team team;
}