package co.jinear.core.service.richtext.sync;

import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoteRichTextSyncAuthStrategy implements RichTextSyncAuthStrategy {

    private final NoteRetrieveService noteRetrieveService;
    private final NotebookRetrieveService notebookRetrieveService;
    private final NotebookAccessValidator notebookAccessValidator;
    private final WorkspaceValidator workspaceValidator;

    @Override
    public RichTextType supports() {
        return RichTextType.NOTE;
    }

    @Override
    public void validateCanRead(String accountId, RichText richText) {
        validateNotebookAccess(accountId, richText);
    }

    @Override
    public void validateCanWrite(String accountId, RichText richText) {
        validateNotebookAccess(accountId, richText);
    }

    private void validateNotebookAccess(String accountId, RichText richText) {
        NoteDto noteDto = noteRetrieveService.retrieve(richText.getRelatedObjectId());
        NotebookDto notebookDto = notebookRetrieveService.retrieve(noteDto.getNotebookId());
        workspaceValidator.validateHasAccess(accountId, notebookDto.getWorkspaceId());
        notebookAccessValidator.validateHasAccess(accountId, notebookDto);
    }
}
