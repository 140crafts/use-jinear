package co.jinear.core.model.entity.richtext;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.richtext.RichTextSourceStack;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "rich_text")
public class RichText extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "rich_text_id")
    private String richTextId;

    @Column(name = "related_object_id")
    private String relatedObjectId;

    @Column(name = "value")
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private RichTextType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_stack")
    private RichTextSourceStack sourceStack;

    @ToString.Exclude
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "related_object_id", insertable = false, updatable = false)
    private Task task;
}