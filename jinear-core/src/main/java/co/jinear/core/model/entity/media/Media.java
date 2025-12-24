package co.jinear.core.model.entity.media;

import co.jinear.core.converter.media.*;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.media.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@FieldNameConstants
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

    @Column(name = "late_owner_id")
    private String lateOwnerId;

    @Convert(converter = MediaVisibilityTypeConverter.class)
    @Column(name = "visibility")
    private MediaVisibilityType visibility;

    @Column(name = "public_until")
    private ZonedDateTime publicUntil;

    @Convert(converter = MediaFileProviderTypeConverter.class)
    @Column(name = "provider_type")
    private MediaFileProviderType providerType;

    @Convert(converter = MediaFileOwnershipStatusTypeConverter.class)
    @Column(name = "ownership_status")
    private MediaFileOwnershipStatusType ownershipStatus;

    @Convert(converter = MediaFileUploadMethodTypeConverter.class)
    @Column(name = "upload_method")
    private MediaFileUploadMethodType uploadMethod;

    @Convert(converter = MediaFileUploadStatusTypeConverter.class)
    @Column(name = "upload_status")
    private MediaFileUploadStatusType uploadStatus;

    @Column(name = "upload_window_expires_at")
    private ZonedDateTime uploadWindowExpiresAt;
}
