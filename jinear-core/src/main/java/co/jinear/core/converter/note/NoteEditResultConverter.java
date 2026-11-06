package co.jinear.core.converter.note;

import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.NoteEditResultDto;
import org.springframework.stereotype.Component;

@Component
public class NoteEditResultConverter {

    public NoteEditResultDto convert(NoteDto note, boolean changed) {
        NoteEditResultDto dto = new NoteEditResultDto();
        dto.setNoteId(note.getNoteId());
        dto.setUrl(note.getUrl());
        dto.setChanged(changed);
        return dto;
    }
}
