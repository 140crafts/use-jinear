package co.jinear.core.model.entity.integration;

import co.jinear.core.converter.integration.IntegrationScopeTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "integration_scope")
public class IntegrationScope extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "integration_scope_id")
    private String integrationScopeId;

    @Column(name = "integration_info_id")
    private String integrationInfoId;

    @Convert(converter = IntegrationScopeTypeConverter.class)
    @Column(name = "scope")
    private IntegrationScopeType scope;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "integration_info_id", insertable = false, updatable = false)
    private IntegrationInfo integrationInfo;
}
