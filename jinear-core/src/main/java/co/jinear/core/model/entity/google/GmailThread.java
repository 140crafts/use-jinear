package co.jinear.core.model.entity.google;

import co.jinear.core.converter.EncryptedColumnConverter;
import co.jinear.core.model.entity.BaseEntity;
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
@Table(name = "gmail_thread")
public class GmailThread extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "gmail_thread_id")
    private String gmailThreadId;

    @Column(name = "google_token_id")
    private String googleTokenId;

    @Column(name = "g_id")
    private String gId;

    @Column(name = "g_history_id")
    private String gHistoryId;

    @Convert(converter = EncryptedColumnConverter.class)
    @Column(name = "snippet")
    private String snippet;

    @OneToMany(mappedBy = "gmailThread")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate desc")
    private Set<GmailMessage> messages;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "google_token_id", insertable = false, updatable = false)
    private GoogleToken googleToken;
}
