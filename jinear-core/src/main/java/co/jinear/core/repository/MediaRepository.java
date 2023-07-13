package co.jinear.core.repository;

import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, String> {

    Optional<Media> findByMediaIdAndPassiveIdIsNull(String mediaId);

    Optional<Media> findFirstByRelatedObjectIdAndFileTypeAndPassiveIdIsNull(String relatedObjectId, FileType fileType);

    Optional<Media> findFirstByMediaKeyAndRelatedObjectIdAndPassiveIdIsNull(String mediaKey, String relatedObjectId);

    Optional<Media> findByMediaIdAndRelatedObjectIdAndPassiveIdIsNull(String mediaId, String relatedObjectId);

    List<Media> findAllByRelatedObjectIdAndFileTypeAndPassiveIdIsNullOrderByCreatedDateAsc(String relatedObjectId, FileType fileType);

    List<Media> findAllByVisibilityAndPublicUntilBeforeAndPassiveIdIsNull(MediaVisibilityType visibility, ZonedDateTime publicUntilBefore);
}
