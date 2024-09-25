package co.jinear.core.model.entity.project;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.entity.richtext.RichText;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "project_post")
public class ProjectPost extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "project_post_id")
    private String projectPostId;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "post_body_rich_text_id")
    private String postBodyRichTextId;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "post_body_rich_text_id", referencedColumnName = "rich_text_id", insertable = false, updatable = false)
    private RichText postBody;

    @OneToMany
    @JoinColumn(name = "project_post_id", referencedColumnName = "related_object_id", insertable = false, updatable = false)
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<Media> files;
}
