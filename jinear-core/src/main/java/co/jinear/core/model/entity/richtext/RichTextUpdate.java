package co.jinear.core.model.entity.richtext;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "rich_text_update")
public class RichTextUpdate {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "rich_text_update_id")
    private String richTextUpdateId;

    @Column(name = "rich_text_id", nullable = false)
    private String richTextId;

    @Column(name = "seq", nullable = false)
    private Long seq;

    @Column(name = "update_bytes", nullable = false)
    private byte[] updateBytes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "idate", nullable = false, insertable = false, updatable = false)
    private Date idate;
}
