package co.jinear.core.validator.note;

import co.jinear.core.exception.note.NoteEditRejectedException;
import co.jinear.core.model.dto.richtext.RichTextDocumentDto;
import co.jinear.core.model.enumtype.richtext.RichTextFormat;
import org.springframework.stereotype.Component;

@Component
public class NoteEditValidator {

    public void validateEditable(RichTextDocumentDto document) {
        if (!RichTextFormat.CRDT.equals(document.getFormat())) {
            throw new NoteEditRejectedException("note.error.edit.old-format");
        }
    }
}
