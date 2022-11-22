package co.jinear.core.config.locale;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageSourceLocalizer {

    private final ResourceBundleMessageSource messageSource;

    public LocaleMessage getLocaleMessage(String key) {
        return getLocaleMessage(key, new Object[0]);
    }

    public LocaleMessage getLocaleMessage(String key, Object... args) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            String errorMessage = messageSource.getMessage(key, args, locale);
            Locale consumerLocale = locale;
            String consumerErrorMessage = messageSource.getMessage(key, args, consumerLocale);

            return new LocaleMessage(errorMessage, getConsumerErrorMessage(errorMessage, consumerErrorMessage));
        } catch (NoSuchMessageException e) {
            log.error(key + " not found in messages file", e);
            return new LocaleMessage(StringUtils.EMPTY, StringUtils.EMPTY);
        }
    }

    private String getConsumerErrorMessage(String errorMessage, String consumerErrorMessage) {
        return Objects.isNull(consumerErrorMessage) ? errorMessage : consumerErrorMessage;
    }
}
