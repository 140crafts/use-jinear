package co.jinear.core.converter.mcp;

import co.jinear.core.model.mcp.input.McpAppendToNoteInput;
import co.jinear.core.model.mcp.input.McpCreateNoteInput;
import co.jinear.core.model.mcp.input.McpMoveNoteInput;
import co.jinear.core.model.mcp.input.McpNoteScopedInput;
import co.jinear.core.model.mcp.input.McpReplaceInNoteInput;
import co.jinear.core.model.mcp.input.McpUpdateNoteTitleInput;
import co.jinear.core.model.request.note.NoteMoveRequest;
import co.jinear.core.model.vo.note.NoteCreateVo;
import co.jinear.core.model.vo.note.NoteEditVo;
import co.jinear.core.model.vo.note.change.AppendBodyChange;
import co.jinear.core.model.vo.note.change.ReplaceTextBodyChange;
import org.springframework.stereotype.Component;

@Component
public class McpNoteEditConverter {

    public NoteCreateVo create(McpCreateNoteInput input) {
        NoteCreateVo vo = new NoteCreateVo();
        vo.setWorkspaceId(input.getWorkspaceId());
        vo.setNotebookId(input.getNotebookId());
        vo.setParentNoteId(input.getParentNoteId());
        vo.setTitle(input.getTitle());
        vo.setBody(input.getBody());
        return vo;
    }

    public NoteEditVo replace(McpReplaceInNoteInput input) {
        NoteEditVo vo = base(input);
        vo.setBodyChange(new ReplaceTextBodyChange(input.getOldText(), input.getNewText()));
        return vo;
    }

    public NoteEditVo append(McpAppendToNoteInput input) {
        NoteEditVo vo = base(input);
        vo.setBodyChange(new AppendBodyChange(input.getBody(), input.getPosition()));
        return vo;
    }

    public NoteEditVo updateTitle(McpUpdateNoteTitleInput input) {
        NoteEditVo vo = base(input);
        vo.setTitle(input.getTitle());
        return vo;
    }

    public NoteMoveRequest move(McpMoveNoteInput input) {
        NoteMoveRequest request = new NoteMoveRequest();
        request.setNoteId(input.getNoteId());
        request.setParentNoteId(input.getParentNoteId());
        return request;
    }

    private NoteEditVo base(McpNoteScopedInput input) {
        NoteEditVo vo = new NoteEditVo();
        vo.setWorkspaceId(input.getWorkspaceId());
        vo.setNoteId(input.getNoteId());
        return vo;
    }
}
