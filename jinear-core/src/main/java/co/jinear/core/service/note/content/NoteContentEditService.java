package co.jinear.core.service.note.content;

import co.jinear.core.converter.note.NoteEditResultConverter;
import co.jinear.core.exception.note.RichTextHeadMovedException;
import co.jinear.core.model.dto.note.NoteContentDto;
import co.jinear.core.model.dto.note.NoteCrdtResultDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.NoteEditResultDto;
import co.jinear.core.model.dto.richtext.RichTextDocumentDto;
import co.jinear.core.model.vo.note.NoteEditVo;
import co.jinear.core.model.vo.note.NoteUpdateVo;
import co.jinear.core.model.vo.richtext.AppendRichTextUpdateVo;
import co.jinear.core.service.note.NoteOperationService;
import co.jinear.core.service.richtext.crdt.NoteCrdtEngine;
import co.jinear.core.service.richtext.sync.RichTextSyncOperationService;
import co.jinear.core.validator.note.NoteEditValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteContentEditService {

    private final RichTextSyncOperationService richTextSyncOperationService;
    private final NoteCrdtEngine noteCrdtEngine;
    private final NoteOperationService noteOperationService;
    private final NoteEditValidator noteEditValidator;
    private final NoteEditResultConverter noteEditResultConverter;

    @Retryable(includes = RichTextHeadMovedException.class, maxRetries = 2, delay = 100)
    public NoteEditResultDto edit(String accountId, String sessionInfoId, NoteDto note, NoteEditVo vo) {
        log.info("Edit note content has started. vo: {}", vo);
        RichTextDocumentDto document = richTextSyncOperationService.retrieveDocument(note.getRichTextId());
        noteEditValidator.validateEditable(document);
        String targetHtml = resolveTargetHtml(document, vo);
        NoteCrdtResultDto result = noteCrdtEngine.edit(document.getStates(), targetHtml, vo.getTitle());
        if (!result.isChanged()) {
            return noteEditResultConverter.convert(note, false);
        }
        AppendRichTextUpdateVo appendVo = mapAppendVo(accountId, sessionInfoId, note, result);
        richTextSyncOperationService.appendUpdateAtHead(appendVo, document.getHeadSeq());
        mirrorTitle(note, result.getTitle());
        return noteEditResultConverter.convert(note, true);
    }

    private AppendRichTextUpdateVo mapAppendVo(String accountId, String sessionInfoId, NoteDto note, NoteCrdtResultDto result) {
        AppendRichTextUpdateVo appendVo = new AppendRichTextUpdateVo();
        appendVo.setRichTextId(note.getRichTextId());
        appendVo.setAccountId(accountId);
        appendVo.setSessionInfoId(sessionInfoId);
        appendVo.setUpdate(result.getUpdate());
        appendVo.setHtml(result.getHtml());
        appendVo.setTitle(result.getTitle());
        return appendVo;
    }

    private String resolveTargetHtml(RichTextDocumentDto document, NoteEditVo vo) {
        if (Objects.isNull(vo.getBodyChange())) {
            return null;
        }
        NoteContentDto current = noteCrdtEngine.read(document.getStates());
        return vo.getBodyChange().apply(current.getHtml());
    }

    private void mirrorTitle(NoteDto note, String documentTitle) {
        Optional.ofNullable(documentTitle)
                .filter(StringUtils::isNotBlank)
                .filter(title -> !title.equals(note.getTitle()))
                .map(title -> new NoteUpdateVo(note.getNoteId(), title))
                .ifPresent(noteOperationService::update);
    }
}
