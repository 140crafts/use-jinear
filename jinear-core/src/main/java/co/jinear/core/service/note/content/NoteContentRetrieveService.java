package co.jinear.core.service.note.content;

import co.jinear.core.converter.note.NoteContentConverter;
import co.jinear.core.exception.note.NoteCrdtException;
import co.jinear.core.model.dto.note.NoteContentDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.richtext.RichTextDocumentDto;
import co.jinear.core.model.enumtype.richtext.RichTextFormat;
import co.jinear.core.service.richtext.crdt.NoteCrdtEngine;
import co.jinear.core.service.richtext.sync.RichTextSyncOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteContentRetrieveService {

    private final RichTextSyncOperationService richTextSyncOperationService;
    private final NoteCrdtEngine noteCrdtEngine;
    private final NoteContentConverter noteContentConverter;

    public NoteContentDto retrieve(NoteDto note) {
        RichTextDocumentDto document = richTextSyncOperationService.retrieveDocument(note.getRichTextId());
        if (!RichTextFormat.CRDT.equals(document.getFormat())) {
            return noteContentConverter.projection(document);
        }
        return readDocumentOrProjection(document);
    }

    private NoteContentDto readDocumentOrProjection(RichTextDocumentDto document) {
        try {
            return noteCrdtEngine.read(document.getStates());
        } catch (NoteCrdtException exception) {
            log.error("Note document read has failed, using the stored projection.", exception);
            return noteContentConverter.projection(document);
        }
    }
}
