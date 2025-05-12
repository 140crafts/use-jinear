package co.jinear.core.model.entity.project;

import co.jinear.core.converter.project.ProjectDomainCnameCheckTypeConverter;
import co.jinear.core.converter.project.ProjectDomainTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.project.ProjectDomainCnameCheckResultType;
import co.jinear.core.model.enumtype.project.ProjectDomainType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "project_domain")
public class ProjectDomain extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "project_domain_id")
    private String projectDomainId;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "domain")
    private String domain;

    @Convert(converter = ProjectDomainTypeConverter.class)
    @Column(name = "domain_type")
    private ProjectDomainType domainType;

    @Convert(converter = ProjectDomainCnameCheckTypeConverter.class)
    @Column(name = "cname_check_result")
    private ProjectDomainCnameCheckResultType cnameCheckResult;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;
}
