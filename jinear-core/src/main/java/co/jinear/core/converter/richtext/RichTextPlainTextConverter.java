package co.jinear.core.converter.richtext;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RichTextPlainTextConverter {

    // Jsoup text() folds every whitespace run into one space, so line breaks go in as this marker and come back out after it.
    private static final String LINE_BREAK_MARKER = "\\n";
    private static final String BLOCK_ELEMENTS = "p, h1, h2, h3, h4, h5, h6, blockquote, pre, tr, div";
    private static final String TASK_ITEM = "li[data-type=taskItem]";
    private static final String TABLE_CELL = "th, td";
    private static final String CELL_SEPARATOR = " | ";

    public String toPlainText(String html) {
        if (StringUtils.isBlank(html)) {
            return "";
        }
        Document document = Jsoup.parse(html);
        document.select("a[href]").forEach(this::appendHref);
        document.select("tr").forEach(this::flattenRow);
        document.select("br").after(LINE_BREAK_MARKER);
        document.select(BLOCK_ELEMENTS).stream()
                .filter(this::startsALine)
                .forEach(element -> element.before(LINE_BREAK_MARKER));
        document.select("li").forEach(this::markListItem);
        String text = document.text().replace(LINE_BREAK_MARKER, "\n");
        return Arrays.stream(text.split("\n"))
                .map(String::strip)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining("\n"));
    }

    private void appendHref(Element link) {
        String href = link.attr("href");
        if (!href.equals(link.text())) {
            link.after(new TextNode(" (" + href + ")"));
        }
    }

    private void flattenRow(Element row) {
        String line = row.children().stream()
                .filter(child -> child.is(TABLE_CELL))
                .map(Element::text)
                .collect(Collectors.joining(CELL_SEPARATOR));
        row.empty().appendText(line);
    }

    private boolean startsALine(Element element) {
        return !opensListItem(element);
    }

    private boolean opensListItem(Element element) {
        Element parent = element.parent();
        boolean textBefore = element.previousElementSiblings().stream().anyMatch(Element::hasText);
        if (Objects.isNull(parent) || textBefore) {
            return false;
        }
        return parent.is("li") || (parent.is(BLOCK_ELEMENTS) && opensListItem(parent));
    }

    private void markListItem(Element listItem) {
        listItem.before(LINE_BREAK_MARKER);
        listItem.prependText(listItemPrefix(listItem));
    }

    private String listItemPrefix(Element listItem) {
        if (listItem.is(TASK_ITEM)) {
            return "true".equals(listItem.attr("data-checked")) ? "[x] " : "[ ] ";
        }
        boolean ordered = Optional.ofNullable(listItem.parent()).filter(parent -> parent.is("ol")).isPresent();
        return ordered ? orderedNumber(listItem) + ". " : "- ";
    }

    private int orderedNumber(Element listItem) {
        int start = Optional.ofNullable(listItem.parent())
                .map(parent -> parent.attr("start"))
                .filter(StringUtils::isNumeric)
                .map(Integer::parseInt)
                .orElse(1);
        return start + listItem.elementSiblingIndex();
    }
}
