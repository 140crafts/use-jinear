package co.jinear.core.note;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.note.NoteDtoConverter;
import co.jinear.core.converter.note.NoteEntityConverter;
import co.jinear.core.converter.note.NoteUrlConverter;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.entity.note.Note;
import co.jinear.core.model.vo.note.NoteInitializeVo;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.repository.note.NoteRepository;
import co.jinear.core.service.note.NoteFtsRefreshService;
import co.jinear.core.service.note.NoteLockService;
import co.jinear.core.service.note.NoteOperationService;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.note.notetag.NoteTagAssignmentOperationService;
import co.jinear.core.service.richtext.RichTextInitializeService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class NoteInitializeUrlTest {

    private final NoteRepository noteRepository = Mockito.mock(NoteRepository.class);
    private final NoteEntityConverter noteEntityConverter = Mockito.mock(NoteEntityConverter.class);
    private final RichTextInitializeService richTextInitializeService = Mockito.mock(RichTextInitializeService.class);
    private final NoteDtoConverter noteDtoConverter = Mockito.mock(NoteDtoConverter.class);
    private final WorkspaceRetrieveService workspaceRetrieveService = Mockito.mock(WorkspaceRetrieveService.class);

    @Test
    void theCreatedNoteGetsItsUrlAlthoughItsWorkspaceRelationIsNotLoaded() {
        NoteOperationService service = service();
        Note note = new Note();
        note.setNoteId("note-1");
        note.setWorkspaceId("ws-1");
        note.setNotebookId("nb-1");
        Mockito.when(noteEntityConverter.convert(any(NoteInitializeVo.class))).thenReturn(note);
        Mockito.when(noteRepository.save(note)).thenReturn(note);
        RichTextDto richText = new RichTextDto();
        richText.setRichTextId("rt-1");
        Mockito.when(richTextInitializeService.initializeRichText(any(InitializeRichTextVo.class))).thenReturn(richText);
        Mockito.when(noteDtoConverter.convert(note)).thenReturn(dtoWithoutUrl());
        WorkspaceDto workspace = new WorkspaceDto();
        workspace.setUsername("acme");
        Mockito.when(workspaceRetrieveService.retrieveWorkspaceWithId("ws-1")).thenReturn(workspace);

        NoteDto created = service.initialize(new NoteInitializeVo());

        assertThat(created.getUrl()).isEqualTo("https://app.jinear.co/acme/notebook/nb-1/note/note-1");
    }

    private NoteOperationService service() {
        FeProperties feProperties = new FeProperties();
        feProperties.setNoteUrl("https://app.jinear.co/{workspaceName}/notebook/{notebookId}/note/{noteId}");
        return new NoteOperationService(
                noteRepository,
                noteEntityConverter,
                Mockito.mock(NoteRetrieveService.class),
                richTextInitializeService,
                Mockito.mock(NoteTagAssignmentOperationService.class),
                noteDtoConverter,
                Mockito.mock(NoteLockService.class),
                Mockito.mock(NoteFtsRefreshService.class),
                workspaceRetrieveService,
                new NoteUrlConverter(feProperties));
    }

    private NoteDto dtoWithoutUrl() {
        NoteDto noteDto = new NoteDto();
        noteDto.setNoteId("note-1");
        noteDto.setWorkspaceId("ws-1");
        noteDto.setNotebookId("nb-1");
        return noteDto;
    }
}
