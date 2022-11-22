package co.jinear.core.repository;

import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.enumtype.media.FileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media,String> {

    Optional<Media> findByMediaIdAndPassiveIdIsNull(String mediaId);

    Optional<Media> findFirstByRelatedObjectIdAndFileTypeAndPassiveIdIsNull(String relatedObjectId, FileType fileType);
}
