package co.jinear.core.model.vo.note.change;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WholeBodyChange implements NoteBodyChange {

    private final String html;

    @Override
    public String apply(String currentHtml) {
        return html;
    }
}
