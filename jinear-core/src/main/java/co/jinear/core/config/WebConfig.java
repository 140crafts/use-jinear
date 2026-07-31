package co.jinear.core.config;

import co.jinear.core.config.interceptor.AcceptLanguageHeaderInterceptor;
import co.jinear.core.config.interceptor.LogExecutionInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.regex.Pattern;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Inline styles the rich text editor emits on table elements: the table/col width the column
     * resizer produces, and the cell alignment. Anything else in a style attribute is dropped.
     */
    private static final String TABLE_STYLE_DECLARATION =
            "(?:min-width|width)\\s*:\\s*\\d+(?:\\.\\d+)?px"
                    + "|text-align\\s*:\\s*(?:left|right|center)";
    private static final Pattern TABLE_STYLE = Pattern.compile(
            "^\\s*(?:" + TABLE_STYLE_DECLARATION + ")(?:\\s*;\\s*(?:" + TABLE_STYLE_DECLARATION + "))*\\s*;?\\s*$",
            Pattern.CASE_INSENSITIVE);
    /** Editor's own scroll container around a table — the only class the sanitizer lets through. */
    private static final Pattern TABLE_WRAPPER_CLASS = Pattern.compile("^tableWrapper$");
    private static final Pattern POSITIVE_INT = Pattern.compile("^\\d+$");
    /**
     * Task lists are the only nodes allowed to carry a data attribute. The editor serializes them
     * without the checkbox element it renders on screen, so these two attributes are all that
     * distinguishes a checklist from a plain bullet list once the value is stored.
     */
    private static final Pattern TASK_LIST_TYPE = Pattern.compile("^task(?:List|Item)$");
    private static final Pattern BOOLEAN = Pattern.compile("^(?:true|false)$");
    /** Tiptap stores resized column widths as a comma separated list of pixel values. */
    private static final Pattern COLWIDTH = Pattern.compile("^\\d+(?:,\\d+)*$");

    @Bean(name = "encryptorBean")
    public StringEncryptor stringEncryptor() {
        String password = System.getenv("jasypt.encryptor.password");
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

    @Bean(name = "htmlSanitizerPolicy")
    PolicyFactory htmlSanitizerPolicy() {
        final String TARGET = "target";
        final String _BLANK = "_blank";

        return new HtmlPolicyBuilder()
                .allowUrlProtocols("https")
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
                }, "h1", "h2", "h3", "h4", "h5", "h6", "p", "b", "i", "em", "strong", "a", "br", "li", "ul", "ol", "blockquote", "hr", "pre", "code", "img")
                .allowElements("table", "thead", "tbody", "tfoot", "tr", "td", "th", "colgroup", "col", "div")
                .allowAttributes("href", TARGET, "rel").onElements("a")
                .allowAttributes("src").onElements("img")
                .allowAttributes("colspan", "rowspan").matching(POSITIVE_INT).onElements("td", "th")
                .allowAttributes("colwidth").matching(COLWIDTH).onElements("td", "th")
                .allowAttributes("style").matching(TABLE_STYLE).onElements("table", "col", "td", "th")
                .allowAttributes("class").matching(TABLE_WRAPPER_CLASS).onElements("div")
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

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogExecutionInterceptor());
        registry.addInterceptor(new AcceptLanguageHeaderInterceptor());
    }
}
