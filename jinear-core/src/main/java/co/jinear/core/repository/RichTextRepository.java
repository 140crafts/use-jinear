package co.jinear.core.repository;

import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RichTextRepository extends JpaRepository<RichText, String> {

    Optional<RichText> findByRichTextIdAndPassiveIdIsNull(String richTextId);

    Optional<RichText> findByRelatedObjectIdAndTypeAndPassiveIdIsNull(String relatedObjectId, RichTextType type);
}
