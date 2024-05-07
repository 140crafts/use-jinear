package co.jinear.core.model.entity.messaging;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "message_data")
public class MessageData extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "message_data_id")
    private String messageDataId;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "data_key")
    private String dataKey;

    @Column(name = "data_value")
    private String dataValue;

    @ManyToOne
    @JoinColumn(name = "message_id", insertable = false, updatable = false)
    private Message message;
}
