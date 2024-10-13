package co.jinear.core.model.entity.project;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.richtext.RichText;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "project_post_comment")
public class ProjectPostComment extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "project_post_comment_id")
    private String projectPostCommentId;

    @Column(name = "project_post_id")
    private String projectPostId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "comment_body_rich_text_id")
    private String commentBodyRichTextId;

    @Column(name = "quote_project_post_comment_id")
    private String quoteProjectPostCommentId;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "project_post_id", insertable = false, updatable = false)
    private ProjectPost projectPost;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "comment_body_rich_text_id", referencedColumnName = "rich_text_id", insertable = false, updatable = false)
    private RichText commentBody;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "quote_project_post_comment_id", referencedColumnName = "project_post_comment_id", insertable = false, updatable = false)
    private ProjectPostComment quote;
}
