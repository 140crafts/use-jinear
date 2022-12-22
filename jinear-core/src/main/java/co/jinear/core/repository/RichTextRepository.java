package co.jinear.core.repository;

import co.jinear.core.model.entity.richtext.RichText;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RichTextRepository extends JpaRepository<RichText, String> {

    Optional<RichText> findByRichTextIdAndPassiveIdIsNull(String richTextId);

}
