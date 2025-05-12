package co.jinear.core.service.richtext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HtmlSanitizeService {

    private final PolicyFactory htmlSanitizerPolicy;
    private final PolicyFactory htmlToPlainTextPolicy;

    public String sanitizeHTML(String untrustedHTML) {
        log.info("SanitizeHTML has started for untrustedHTML: {}", untrustedHTML);
        String result = htmlSanitizerPolicy.sanitize(untrustedHTML);
        log.info("SanitizeHTML has completed. result: {}", result);
        return result;
    }

    public String toPlainText(String html){
        return htmlToPlainTextPolicy.sanitize(html);
    }
}
