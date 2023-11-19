package co.jinear.core.model.entity.integration;

import co.jinear.core.converter.integration.IntegrationProviderConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "integration_info")
public class IntegrationInfo extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "integration_info_id")
    private String integrationInfoId;

    @Convert(converter = IntegrationProviderConverter.class)
    @Column(name = "provider")
    private IntegrationProvider provider;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "related_object_id")
    private String relatedObjectId;

    @OneToMany(mappedBy = "integrationInfo")
    @Where(clause = "passive_id is null")
    private Set<IntegrationScope> scopes;
}
