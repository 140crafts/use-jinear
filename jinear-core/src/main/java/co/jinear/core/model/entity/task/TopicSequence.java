package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "topic_seq")
public class TopicSequence extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "topic_seq_id")
    private Long topicSeqId;

    @Column(name = "topic_id")
    private String topicId;

    @Column(name = "seq")
    private Integer seq;
}
