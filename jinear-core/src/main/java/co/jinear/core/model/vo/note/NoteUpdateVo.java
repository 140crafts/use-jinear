package co.jinear.core.model.vo.note;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoteUpdateVo {

    private String noteId;
    private String notebookId;
    private String title;
}
