package co.jinear.core.model.entity.richtext;

import co.jinear.core.converter.richtext.RichTextFormatConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.richtext.RichTextFormat;
import co.jinear.core.model.enumtype.richtext.RichTextSourceStack;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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

    @Convert(converter = RichTextFormatConverter.class)
    @Column(name = "format", nullable = false)
    private RichTextFormat format = RichTextFormat.LEGACY;

    @Column(name = "yjs_state")
    private byte[] yjsState;

    @Column(name = "yjs_state_seq", nullable = false)
    private long yjsStateSeq;

    /**
     * Per-rich-text high-water mark of assigned {@code rich_text_update.seq} values. Monotonic, never
     * decremented (survives compaction), so the next append seq is always {@code updateSeq + 1}.
     */
    @Column(name = "update_seq", nullable = false)
    private long updateSeq;
}