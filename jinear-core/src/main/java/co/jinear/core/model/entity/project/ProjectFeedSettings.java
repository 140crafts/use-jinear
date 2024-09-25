package co.jinear.core.model.entity.project;

import co.jinear.core.converter.project.ProjectFeedAccessTypeConverter;
import co.jinear.core.converter.project.ProjectPostInitializeAccessTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.project.ProjectFeedAccessType;
import co.jinear.core.model.enumtype.project.ProjectPostInitializeAccessType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "project_feed_settings")
public class ProjectFeedSettings extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "project_feed_settings_id")
    private String projectFeedSettingsId;

    @Column(name = "project_id")
    private String projectId;

    @Convert(converter = ProjectFeedAccessTypeConverter.class)
    @Column(name = "project_feed_access_type")
    private ProjectFeedAccessType projectFeedAccessType;

    @Convert(converter = ProjectPostInitializeAccessTypeConverter.class)
    @Column(name = "project_post_initialize_access_type")
    private ProjectPostInitializeAccessType projectPostInitializeAccessType;

    @Column(name = "info_rich_text_id")
    private String infoRichTextId;

    @Column(name = "info_website_url")
    private String infoWebsiteUrl;

    @Column(name = "access_key")
    private String accessKey;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "info_rich_text_id", referencedColumnName = "rich_text_id", insertable = false, updatable = false)
    private RichText info;
}
