package co.jinear.core.converter.richtext;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class RichTextPlainTextConverter {

    // Jsoup text() folds every whitespace run into one space, so line breaks go in as this marker and come back out after it.
    private static final String LINE_BREAK_MARKER = "\\n";
    private static final String LIST_ITEM_PARAGRAPH = "li > p";
    private static final String BLOCK_ELEMENTS = "p, h1, h2, h3, h4, h5, h6, blockquote, pre, tr, div";

    public String toPlainText(String html) {
        if (StringUtils.isBlank(html)) {
            return "";
        }
        Document document = Jsoup.parse(html);
        document.select("br").after(LINE_BREAK_MARKER);
        document.select(BLOCK_ELEMENTS).stream()
                .filter(this::startsALine)
                .forEach(element -> element.before(LINE_BREAK_MARKER));
        document.select("li").before(LINE_BREAK_MARKER + "- ");
        String text = document.text().replace(LINE_BREAK_MARKER, "\n");
        return Arrays.stream(text.split("\n"))
                .map(String::strip)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining("\n"));
    }

    private boolean startsALine(Element element) {
        return !element.is(LIST_ITEM_PARAGRAPH);
    }
}
