package co.jinear.core.model.entity.token;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.token.TokenType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "token")
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "related_object")
    private String relatedObject;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    private TokenType tokenType;

    @Column(name = "unique_token", unique = true)
    private String uniqueToken;

    @Column(name = "common_token")
    private String commonToken;

    @Column(name = "additional_data")
    private String additionalData;

    @Column(name = "expires_at")
    private Long expiresAt;
}
