package co.jinear.core.service.richtext.sync;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.richtext.RichTextAppendDto;
import co.jinear.core.model.dto.richtext.RichTextStateDto;
import co.jinear.core.model.dto.richtext.RichTextUpdatesDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.entity.richtext.RichTextUpdate;
import co.jinear.core.model.enumtype.richtext.RichTextFormat;
import co.jinear.core.repository.RichTextRepository;
import co.jinear.core.repository.richtext.RichTextUpdateRepository;
import co.jinear.core.service.richtext.HtmlSanitizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextSyncService {

    private final RichTextRepository richTextRepository;
    private final RichTextUpdateRepository richTextUpdateRepository;
    private final HtmlSanitizeService htmlSanitizeService;

    @Transactional(readOnly = true)
    public RichTextStateDto retrieveState(String richTextId) {
        RichText richText = retrieve(richTextId);
        return toStateDto(richText);
    }

    @Transactional(readOnly = true)
    public RichTextUpdatesDto getUpdates(String richTextId, long since) {
        RichText richText = retrieve(richTextId);
        List<String> updates = richTextUpdateRepository.findByRichTextIdAndSeqGreaterThanOrderBySeqAsc(richTextId, since)
                .stream()
                .map(RichTextUpdate::getUpdateBytes)
                .map(this::encode)
                .toList();
        long headSeq = richText.getHeadSeq();
        RichTextUpdatesDto dto = new RichTextUpdatesDto();
        dto.setHeadSeq(headSeq);
        dto.setUpdates(updates);
        return dto;
    }

    @Transactional
    public RichTextAppendDto appendUpdate(String richTextId, String updateBase64, String renderedHtml) {
        RichText richText = retrieve(richTextId);
        if (!RichTextFormat.CRDT.equals(richText.getFormat())) {
            log.info("Append update rejected on non-CRDT rich text. richTextId: {}, format: {}", richTextId, richText.getFormat());
            throw new NotValidException();
        }
        long seq = richText.getUpdateSeq() + 1;
        richText.setUpdateSeq(seq);
        applyProjectionIfPresent(richText, renderedHtml);
        richTextRepository.save(richText);

        RichTextUpdate richTextUpdate = new RichTextUpdate();
        richTextUpdate.setRichTextId(richTextId);
        richTextUpdate.setSeq(seq);
        richTextUpdate.setUpdateBytes(decode(updateBase64));
        richTextUpdateRepository.save(richTextUpdate);

        log.info("Append update completed. richTextId: {}, seq: {}", richTextId, seq);
        RichTextAppendDto dto = new RichTextAppendDto();
        dto.setSeq(seq);
        return dto;
    }

    @Transactional
    public RichTextStateDto seed(String richTextId, String seedBase64) {
        RichText richText = retrieve(richTextId);
        if (RichTextFormat.CRDT.equals(richText.getFormat())) {
            log.info("Seed rejected, rich text already CRDT. Returning existing state. richTextId: {}", richTextId);
            return toStateDto(richText);
        }
        byte[] yjsState = decode(seedBase64);
        richText.setYjsState(yjsState);
        richText.setYjsStateSeq(0L);
        richText.setFormat(RichTextFormat.CRDT);
        richTextRepository.save(richText);
        log.info("Seed completed. richTextId: {}, yjsStateSeq: {}", richTextId, richText.getYjsStateSeq());
        return toStateDto(richText);
    }

    @Transactional
    public void snapshot(String richTextId, String mergedBase64, long upToSeq) {
        RichText richText = retrieve(richTextId);
        if (!RichTextFormat.CRDT.equals(richText.getFormat())) {
            log.info("Snapshot rejected on non-CRDT rich text. richTextId: {}, format: {}", richTextId, richText.getFormat());
            throw new NotValidException();
        }
        long target = Math.min(upToSeq, richText.getHeadSeq());
        if (target <= richText.getYjsStateSeq()) {
            log.info("Snapshot is stale or no-op, skipping. richTextId: {}, target: {}, currentSeq: {}", richTextId, target, richText.getYjsStateSeq());
            return;
        }
        byte[] yjsState = decode(mergedBase64);
        richText.setYjsState(yjsState);
        richText.setYjsStateSeq(target);
        richTextRepository.save(richText);
        richTextUpdateRepository.deleteByRichTextIdAndSeqLessThanEqual(richTextId, target);
        log.info("Snapshot completed. richTextId: {}, yjsStateSeq: {}", richTextId, target);
    }

    private void applyProjectionIfPresent(RichText richText, String renderedHtml) {
        if (Objects.isNull(renderedHtml)) {
            return;
        }
        richText.setValue(htmlSanitizeService.sanitizeHTML(renderedHtml));
    }

    private RichTextStateDto toStateDto(RichText richText) {
        long headSeq = richText.getHeadSeq();
        RichTextStateDto dto = new RichTextStateDto();
        dto.setFormat(richText.getFormat());
        dto.setSnapshotSeq(richText.getYjsStateSeq());
        dto.setHeadSeq(headSeq);
        Optional.of(richText)
                .map(RichText::getYjsState)
                .map(this::encode)
                .ifPresent(dto::setSnapshot);
        return dto;
    }

    private RichText retrieve(String richTextId) {
        return richTextRepository.findByRichTextIdAndPassiveIdIsNull(richTextId)
                .orElseThrow(NotFoundException::new);
    }

    private String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
