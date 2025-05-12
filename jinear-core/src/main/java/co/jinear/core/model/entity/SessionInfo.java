package co.jinear.core.model.entity;

import co.jinear.core.model.enumtype.auth.ProviderType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "session_info")
public class SessionInfo extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "session_info_id")
    private String sessionInfoId;

    @Column(name = "account_id")
    private String accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private ProviderType provider;

    @Column(name = "token")
    private String token;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "remote_addr")
    private String remoteAddr;
}
