package co.jinear.core.model.dto.note;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.notetag.NoteTagDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

@Getter
@Setter
@ToString
public class NoteDto extends BaseDto {

    private String noteId;
    private String notebookId;
    private String workspaceId;
    private String ownerId;
    private String parentNoteId;
    private String title;
    private String richTextId;
    @Nullable
    private Set<NoteTagDto> tags;
    @Nullable
    private PlainAccountProfileDto owner;
}
