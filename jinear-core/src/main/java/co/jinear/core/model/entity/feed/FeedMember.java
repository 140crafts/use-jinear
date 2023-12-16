package co.jinear.core.model.entity.feed;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.workspace.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "feed_member")
public class FeedMember extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "feed_member_id")
    private String feedMemberId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "feed_id")
    private String feedId;

    @ManyToOne
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "feed_id", insertable = false, updatable = false)
    private Feed feed;
}
