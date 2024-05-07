package co.jinear.core.model.entity.messaging;

import co.jinear.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "conversation")
public class Conversation extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "last_activity_time")
    private ZonedDateTime lastActivityTime;

    @Column(name = "participants_key")
    private String participantsKey;

    @OneToOne(mappedBy = "conversation")
    private ConversationMessageInfo conversationMessageInfo;

    @OneToMany
    @JoinColumn(name = "conversation_id", referencedColumnName = "conversation_id", insertable = false, updatable = false)
    @Where(clause = "passive_id is null and left_at is null")
    @OrderBy("createdDate ASC")
    private Set<ConversationParticipant> participants;
}
