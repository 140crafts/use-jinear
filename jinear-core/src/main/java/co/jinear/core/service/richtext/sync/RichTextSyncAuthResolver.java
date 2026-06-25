package co.jinear.core.service.richtext.sync;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RichTextSyncAuthResolver {

    private final Map<RichTextType, RichTextSyncAuthStrategy> strategies;

    public RichTextSyncAuthResolver(List<RichTextSyncAuthStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(RichTextSyncAuthStrategy::supports, Function.identity()));
    }

    public void validateCanRead(String accountId, RichText richText) {
        resolveStrategy(richText).validateCanRead(accountId, richText);
    }

    public void validateCanWrite(String accountId, RichText richText) {
        resolveStrategy(richText).validateCanWrite(accountId, richText);
    }

    private RichTextSyncAuthStrategy resolveStrategy(RichText richText) {
        RichTextSyncAuthStrategy strategy = strategies.get(richText.getType());
        if (Objects.isNull(strategy)) {
            log.info("No rich text sync auth strategy registered. richTextId: {}, type: {}", richText.getRichTextId(), richText.getType());
            throw new NoAccessException();
        }
        return strategy;
    }
}
