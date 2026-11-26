package co.jinear.core.config;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class HtmlSanitizerConfig {

    private static final String TABLE_STYLE_DECLARATION =
            "(?:min-width|width)\\s*:\\s*\\d+(?:\\.\\d+)?px"
                    + "|text-align\\s*:\\s*(?:left|right|center)";
    private static final Pattern TABLE_STYLE = Pattern.compile(
            "^\\s*(?:" + TABLE_STYLE_DECLARATION + ")(?:\\s*;\\s*(?:" + TABLE_STYLE_DECLARATION + "))*\\s*;?\\s*$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern TABLE_WRAPPER_CLASS = Pattern.compile("^tableWrapper$");
    private static final Pattern POSITIVE_INT = Pattern.compile("^\\d+$");
    private static final Pattern TASK_LIST_TYPE = Pattern.compile("^task(?:List|Item)$");
    private static final Pattern BOOLEAN = Pattern.compile("^(?:true|false)$");
    private static final Pattern COLWIDTH = Pattern.compile("^\\d+(?:,\\d+)*$");
    private static final Pattern CODE_LANGUAGE_CLASS = Pattern.compile("^language-[a-z0-9_+#-]+$", Pattern.CASE_INSENSITIVE);

    @Bean(name = "htmlSanitizerPolicy")
    PolicyFactory htmlSanitizerPolicy() {
        final String TARGET = "target";
        final String _BLANK = "_blank";

        return new HtmlPolicyBuilder()
                .allowUrlProtocols("https", "http")
                .allowElements((elementName, attrs) -> {
                    if ("a".equalsIgnoreCase(elementName)) {
                        int targetIndex = attrs.indexOf(TARGET);
                        if (targetIndex < 0) {
                            attrs.add(TARGET);
                            attrs.add(_BLANK);
                        } else {
                            attrs.set(targetIndex + 1, _BLANK);
                        }
                    }
                    return elementName;
                }, "h1", "h2", "h3", "h4", "h5", "h6", "p", "b", "i", "em", "strong", "s", "u", "a", "br", "li", "ul", "ol", "blockquote", "hr", "pre", "code", "img")
                .allowElements("table", "thead", "tbody", "tfoot", "tr", "td", "th", "colgroup", "col", "div")
                .allowAttributes("href", TARGET, "rel").onElements("a")
                .allowAttributes("src").onElements("img")
                .allowAttributes("colspan", "rowspan").matching(POSITIVE_INT).onElements("td", "th")
                .allowAttributes("colwidth").matching(COLWIDTH).onElements("td", "th")
                .allowAttributes("style").matching(TABLE_STYLE).onElements("table", "col", "td", "th")
                .allowAttributes("class").matching(TABLE_WRAPPER_CLASS).onElements("div")
                .allowAttributes("class").matching(CODE_LANGUAGE_CLASS).onElements("code")
                .allowAttributes("data-type").matching(TASK_LIST_TYPE).onElements("ul", "li")
                .allowAttributes("data-checked").matching(BOOLEAN).onElements("li")
                .requireRelNofollowOnLinks()
                .toFactory();
    }

    @Bean(name = "htmlToPlainTextPolicy")
    PolicyFactory htmlToPlainTextPolicy() {
        return new HtmlPolicyBuilder()
                .toFactory();
    }
}
