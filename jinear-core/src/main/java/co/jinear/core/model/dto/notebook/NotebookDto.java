package co.jinear.core.model.dto.notebook;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.entity.note.NoteTagAssignment;
import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

@Getter
@Setter
@ToString
public class NotebookDto extends BaseDto {

    private String notebookId;
    private String workspaceId;
    private String ownerId;
    private String title;
    private String description;
    private NotebookVisibilityType visibility;
    @Nullable
    private PlainAccountProfileDto owner;
    private Set<NoteTagAssignment> noteTagAssignments;
}
