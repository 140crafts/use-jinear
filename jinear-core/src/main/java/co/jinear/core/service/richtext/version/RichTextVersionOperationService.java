package co.jinear.core.service.richtext.version;

import co.jinear.core.model.entity.richtext.RichTextVersion;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.richtext.RecordRichTextVersionVo;
import co.jinear.core.repository.richtext.RichTextVersionRepository;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.EnumSet;
import java.util.Objects;
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
    public void record(RecordRichTextVersionVo vo) {
        if (!VERSIONED_TYPES.contains(vo.getType()) || isSameAsLatest(vo)) {
            return;
        }
        RichTextVersion version = retrieveOpenVersion(vo)
                .orElseGet(() -> initialize(vo));
        version.setHtml(vo.getHtml());
        version.setTitle(vo.getTitle());
        RichTextVersion saved = richTextVersionRepository.save(version);
        log.info("Record rich text version completed. richTextId: {}, richTextVersionId: {}", vo.getRichTextId(), saved.getRichTextVersionId());
    }

    private boolean isSameAsLatest(RecordRichTextVersionVo vo) {
        return richTextVersionRepository.findFirstByRichTextIdAndPassiveIdIsNullOrderByCreatedDateDesc(vo.getRichTextId())
                .filter(latest -> hasSameContent(latest, vo))
                .isPresent();
    }

    private boolean hasSameContent(RichTextVersion latest, RecordRichTextVersionVo vo) {
        return Objects.equals(latest.getHtml(), vo.getHtml()) && Objects.equals(latest.getTitle(), vo.getTitle());
    }

    private Optional<RichTextVersion> retrieveOpenVersion(RecordRichTextVersionVo vo) {
        Date windowStart = DateHelper.substractMinutes(DateHelper.now(), VERSION_WINDOW_MINUTES);
        return richTextVersionRepository.findFirstByRichTextIdAndAccountIdAndSessionInfoIdAndCreatedDateAfterAndPassiveIdIsNullOrderByCreatedDateDesc(vo.getRichTextId(), vo.getAccountId(), vo.getSessionInfoId(), windowStart);
    }

    private RichTextVersion initialize(RecordRichTextVersionVo vo) {
        RichTextVersion version = new RichTextVersion();
        version.setRichTextId(vo.getRichTextId());
        version.setAccountId(vo.getAccountId());
        version.setSessionInfoId(vo.getSessionInfoId());
        return version;
    }
}
