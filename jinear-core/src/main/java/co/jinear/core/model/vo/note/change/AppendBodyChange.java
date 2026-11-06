package co.jinear.core.model.vo.note.change;

import co.jinear.core.model.enumtype.note.NoteAppendPosition;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppendBodyChange implements NoteBodyChange {

    private final String html;
    private final NoteAppendPosition position;

    @Override
    public String apply(String currentHtml) {
        return NoteAppendPosition.START.equals(position) ? html + currentHtml : currentHtml + html;
    }
}
