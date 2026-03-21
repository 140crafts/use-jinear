package co.jinear.core.repository;

import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaFileUploadMethodType;
import co.jinear.core.model.enumtype.media.MediaFileUploadStatusType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, String> {

    Optional<Media> findByMediaIdAndPassiveIdIsNull(String mediaId);

    Optional<Media> findFirstByRelatedObjectIdAndFileTypeAndPassiveIdIsNullOrderByCreatedDateDesc(String relatedObjectId, FileType fileType);

    Optional<Media> findFirstByMediaKeyAndRelatedObjectIdAndPassiveIdIsNull(String mediaKey, String relatedObjectId);

    Optional<Media> findByMediaIdAndRelatedObjectIdAndUploadStatusAndPassiveIdIsNull(String mediaId, String relatedObjectId, MediaFileUploadStatusType uploadStatus);

    Optional<Media> findByMediaIdAndRelatedObjectIdAndUploadStatus(String mediaId, String relatedObjectId, MediaFileUploadStatusType uploadStatus);

    List<Media> findAllByRelatedObjectIdAndFileTypeAndUploadStatusAndPassiveIdIsNullOrderByCreatedDateAsc(String relatedObjectId, FileType fileType, MediaFileUploadStatusType uploadStatus);

    List<Media> findAllByVisibilityAndPublicUntilBeforeAndPassiveIdIsNull(MediaVisibilityType visibility, ZonedDateTime publicUntilBefore);

    Optional<Media> findFirstByRelatedObjectIdAndMediaKeyAndPassiveIdIsNull(String relatedObjectId, String mediaKey);

    boolean existsByMediaIdAndRelatedObjectIdAndPassiveIdIsNull(String mediaId, String relatedObjectId);

    Page<Media> findAllByUploadMethodAndUploadStatusAndPassiveIdIsNullOrderByCreatedDateDesc(MediaFileUploadMethodType uploadMethod, MediaFileUploadStatusType uploadStatus, Pageable pageable);

    List<Media> findAllByRelatedObjectIdInAndFileTypeInAndUploadStatusAndPassiveIdIsNullOrderByCreatedDateAsc(Collection<String> relatedObjectIds, Collection<FileType> fileTypes, MediaFileUploadStatusType uploadStatus);
}
