package co.jinear.core.model.entity.messaging;

import co.jinear.core.converter.messaging.ChannelMemberRoleTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.enumtype.messaging.ChannelMemberRoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "channel_member")
public class ChannelMember extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "channel_member_id")
    private String channelMemberId;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "account_id")
    private String accountId;

    @Convert(converter = ChannelMemberRoleTypeConverter.class)
    @Column(name = "role_type")
    private ChannelMemberRoleType roleType;

    @Column(name = "silent_until")
    private ZonedDateTime silentUntil;

    @Column(name = "last_check")
    private ZonedDateTime lastCheck;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private Channel channel;
}
