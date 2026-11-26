package co.jinear.core.repository.richtext;

import co.jinear.core.model.entity.richtext.RichTextVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface RichTextVersionRepository extends JpaRepository<RichTextVersion, String> {

    Optional<RichTextVersion> findFirstByRichTextIdAndPassiveIdIsNullOrderByCreatedDateDesc(String richTextId);

    Optional<RichTextVersion> findFirstByRichTextIdAndAccountIdAndSessionInfoIdAndCreatedDateAfterAndPassiveIdIsNullOrderByCreatedDateDesc(String richTextId, String accountId, String sessionInfoId, Date createdAfter);

    Optional<RichTextVersion> findByRichTextVersionIdAndRichTextIdAndPassiveIdIsNull(String richTextVersionId, String richTextId);

    Page<RichTextVersion> findAllByRichTextIdAndPassiveIdIsNullOrderByCreatedDateDesc(String richTextId, Pageable pageable);
}
