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
    void anEmptyBodyGivesAnEmptyString() {
        assertThat(converter.toPlainText(null)).isEmpty();
        assertThat(converter.toPlainText("  ")).isEmpty();
    }
}
