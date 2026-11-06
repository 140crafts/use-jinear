package co.jinear.core.converter.note;

import co.jinear.core.model.dto.note.NoteContentDto;
import co.jinear.core.model.dto.richtext.RichTextDocumentDto;
import org.springframework.stereotype.Component;

@Component
public class NoteContentConverter {

    public NoteContentDto projection(RichTextDocumentDto document) {
        NoteContentDto dto = new NoteContentDto();
        dto.setHtml(document.getValue());
        return dto;
    }
}
