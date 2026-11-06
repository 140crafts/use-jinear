package co.jinear.core.model.vo.note.change;

import co.jinear.core.exception.note.NoteEditRejectedException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class ReplaceTextBodyChange implements NoteBodyChange {

    private final String oldText;
    private final String newText;

    @Override
    public String apply(String currentHtml) {
        int matches = StringUtils.countMatches(currentHtml, oldText);
        if (matches != 1) {
            throw new NoteEditRejectedException("note.error.edit.text-match-count", matches);
        }
        return currentHtml.replace(oldText, newText);
    }
}
