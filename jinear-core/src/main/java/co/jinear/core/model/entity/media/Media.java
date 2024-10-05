package co.jinear.core.model.entity.media;

import co.jinear.core.converter.media.MediaFileProviderTypeConverter;
import co.jinear.core.converter.media.MediaVisibilityTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "media")
public class Media extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "media_id")
    private String mediaId;

    @Column(name = "media_key")
    private String mediaKey;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "related_object_id")
    private String relatedObjectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_owner_type")
    private MediaOwnerType mediaOwnerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileType fileType;

    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "size")
    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @Convert(converter = MediaVisibilityTypeConverter.class)
    @Column(name = "visibility")
    private MediaVisibilityType visibility;

    @Column(name = "public_until")
    private ZonedDateTime publicUntil;

    @Convert(converter = MediaFileProviderTypeConverter.class)
    @Column(name = "provider_type")
    private MediaFileProviderType providerType;
}
