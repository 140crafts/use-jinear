package co.jinear.core.note;

import co.jinear.core.exception.note.NoteEditRejectedException;
import co.jinear.core.model.enumtype.note.NoteAppendPosition;
import co.jinear.core.model.vo.note.change.AppendBodyChange;
import co.jinear.core.model.vo.note.change.ReplaceTextBodyChange;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NoteBodyChangeTest {

    @Test
    void replacesTheOnlyMatch() {
        assertThat(new ReplaceTextBodyChange("two", "TWO").apply("<p>one two</p>")).isEqualTo("<p>one TWO</p>");
    }

    @Test
    void rejectsZeroOrManyMatches() {
        ReplaceTextBodyChange change = new ReplaceTextBodyChange("a", "b");

        assertThatThrownBy(() -> change.apply("<p>x</p>"))
                .isInstanceOf(NoteEditRejectedException.class)
                .hasMessage("note.error.edit.text-match-count")
                .extracting("arguments").isEqualTo(new Object[]{0});
        assertThatThrownBy(() -> change.apply("<p>a a</p>"))
                .isInstanceOf(NoteEditRejectedException.class)
                .hasMessage("note.error.edit.text-match-count")
                .extracting("arguments").isEqualTo(new Object[]{2});
    }

    @Test
    void appendsAtEndByDefault() {
        assertThat(new AppendBodyChange("<p>b</p>", null).apply("<p>a</p>")).isEqualTo("<p>a</p><p>b</p>");
        assertThat(new AppendBodyChange("<p>b</p>", NoteAppendPosition.START).apply("<p>a</p>")).isEqualTo("<p>b</p><p>a</p>");
    }
}
