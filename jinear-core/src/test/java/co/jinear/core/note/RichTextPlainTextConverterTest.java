package co.jinear.core.note;

import co.jinear.core.converter.richtext.RichTextPlainTextConverter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RichTextPlainTextConverterTest {

    private final RichTextPlainTextConverter converter = new RichTextPlainTextConverter();

    @Test
    void blocksAndListItemsBecomeLinesAndEntitiesAreDecoded() {
        String html = "<h1>Plan</h1><p>Tom&#39;s <strong>first</strong> step<br>second line</p><ul><li><p>one</p></li><li><p>two &amp; three</p></li></ul>";

        String text = converter.toPlainText(html);

        assertThat(text).isEqualTo("Plan\nTom's first step\nsecond line\n- one\n- two & three");
    }

    @Test
    void orderedListsKeepTheirNumbersFromTheStartAttribute() {
        String html = "<ol><li><p>first</p></li><li><p>second</p></li></ol><ol start=\"4\"><li><p>fourth</p></li></ol>";

        String text = converter.toPlainText(html);

        assertThat(text).isEqualTo("1. first\n2. second\n4. fourth");
    }

    @Test
    void taskItemsKeepTheirCheckedStateOnOneLine() {
        String html = "<ul data-type=\"taskList\">"
                      + "<li data-checked=\"true\" data-type=\"taskItem\"><label><input type=\"checkbox\" checked><span></span></label><div><p>done okapi</p></div></li>"
                      + "<li data-checked=\"false\" data-type=\"taskItem\"><div><p>open okapi</p></div></li></ul>";

        String text = converter.toPlainText(html);

        assertThat(text).isEqualTo("[x] done okapi\n[ ] open okapi");
    }

    @Test
    void linksKeepTheirUrlOnceAndNestedListsGetTheirOwnLines() {
        String html = "<p>See <a href=\"https://jinear.co/docs\">the docs</a> and <a href=\"https://jinear.co\">https://jinear.co</a></p>"
                      + "<ul><li><p>outer</p><ul><li><p>inner</p></li></ul></li></ul>";

        String text = converter.toPlainText(html);

        assertThat(text).isEqualTo("See the docs (https://jinear.co/docs) and https://jinear.co\n- outer\n- inner");
    }

    @Test
    void anEmptyBodyGivesAnEmptyString() {
        assertThat(converter.toPlainText(null)).isEmpty();
        assertThat(converter.toPlainText("  ")).isEmpty();
    }
}
