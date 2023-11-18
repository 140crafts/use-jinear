package co.jinear.core.model.entity.google;

import co.jinear.core.converter.google.GoogleScopeTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.google.GoogleScopeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "google_token_scope")
public class GoogleTokenScope extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "google_token_scope_id")
    private String googleTokenScopeId;

    @Column(name = "google_token_id")
    private String googleTokenId;

    @Convert(converter = GoogleScopeTypeConverter.class)
    @Column(name = "scope")
    private GoogleScopeType scope;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "google_token_id", insertable = false, updatable = false)
    private GoogleToken googleToken;
}
