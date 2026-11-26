package co.jinear.core.config;

import org.junit.jupiter.api.Test;
import org.owasp.html.PolicyFactory;

import static org.assertj.core.api.Assertions.assertThat;

class HtmlSanitizerPolicyTest {

    private final PolicyFactory policy = new HtmlSanitizerConfig().htmlSanitizerPolicy();

    @Test
    void anHttpLinkKeepsItsHref() {
        String sanitized = policy.sanitize("<p><a href=\"http://localhost:5173/workspace/task/jin-1\">JIN-1</a></p>");

        assertThat(sanitized).contains("href=\"http://localhost:5173/workspace/task/jin-1\"");
    }

    @Test
    void anHttpsLinkKeepsItsHref() {
        String sanitized = policy.sanitize("<p><a href=\"https://app.jinear.co/workspace/task/jin-1\">JIN-1</a></p>");

        assertThat(sanitized).contains("href=\"https://app.jinear.co/workspace/task/jin-1\"");
    }

    @Test
    void aJavascriptLinkLosesItsHref() {
        String sanitized = policy.sanitize("<p><a href=\"javascript:alert(1)\">x</a></p>");

        assertThat(sanitized).doesNotContain("javascript");
    }

    @Test
    void strikeAndUnderlineAreKept() {
        String sanitized = policy.sanitize("<p><s>strike</s> <u>under</u></p>");

        assertThat(sanitized).isEqualTo("<p><s>strike</s> <u>under</u></p>");
    }

    @Test
    void aCodeLanguageClassIsKept() {
        String sanitized = policy.sanitize("<pre><code class=\"language-js\">x</code></pre>");

        assertThat(sanitized).isEqualTo("<pre><code class=\"language-js\">x</code></pre>");
    }

    @Test
    void anotherCodeClassIsDropped() {
        String sanitized = policy.sanitize("<pre><code class=\"evil\">x</code></pre>");

        assertThat(sanitized).isEqualTo("<pre><code>x</code></pre>");
    }

    @Test
    void anHttpImageKeepsItsSourceAndLosesItsHandlers() {
        String sanitized = policy.sanitize("<p><img src=\"http://minio.local/a.png\" onerror=\"alert(1)\"></p>");

        assertThat(sanitized).contains("src=\"http://minio.local/a.png\"").doesNotContain("onerror");
    }

    @Test
    void aScriptIsRemoved() {
        String sanitized = policy.sanitize("<p>text</p><script>alert(1)</script>");

        assertThat(sanitized).isEqualTo("<p>text</p>");
    }
}
