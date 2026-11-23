package co.jinear.core.service.richtext.version;

import co.jinear.core.model.entity.richtext.RichTextVersion;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.repository.richtext.RichTextVersionRepository;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextVersionOperationService {

    private static final Set<RichTextType> VERSIONED_TYPES = EnumSet.of(RichTextType.NOTE);
    private static final int VERSION_WINDOW_MINUTES = 1;

    private final RichTextVersionRepository richTextVersionRepository;

    @Transactional
    public void record(String richTextId, RichTextType type, String accountId, String html) {
        if (!VERSIONED_TYPES.contains(type) || isSameAsLatest(richTextId, html)) {
            return;
        }
        RichTextVersion version = retrieveOpenVersion(richTextId, accountId)
                .orElseGet(() -> initialize(richTextId, accountId));
        version.setHtml(html);
        RichTextVersion saved = richTextVersionRepository.save(version);
        log.info("Record rich text version completed. richTextId: {}, richTextVersionId: {}", richTextId, saved.getRichTextVersionId());
    }

    private boolean isSameAsLatest(String richTextId, String html) {
        return richTextVersionRepository.findFirstByRichTextIdAndPassiveIdIsNullOrderByCreatedDateDesc(richTextId)
                .map(RichTextVersion::getHtml)
                .filter(html::equals)
                .isPresent();
    }

    private Optional<RichTextVersion> retrieveOpenVersion(String richTextId, String accountId) {
        Date windowStart = DateHelper.substractMinutes(DateHelper.now(), VERSION_WINDOW_MINUTES);
        return richTextVersionRepository.findFirstByRichTextIdAndAccountIdAndCreatedDateAfterAndPassiveIdIsNullOrderByCreatedDateDesc(richTextId, accountId, windowStart);
    }

    private RichTextVersion initialize(String richTextId, String accountId) {
        RichTextVersion version = new RichTextVersion();
        version.setRichTextId(richTextId);
        version.setAccountId(accountId);
        return version;
    }
}
