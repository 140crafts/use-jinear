package co.jinear.core.service.mail;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.LocaleString;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.repository.LocaleStringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocaleStringService {

    private static final LocaleType DEFAULT_LOCALE = LocaleType.EN;

    private final LocaleStringRepository localeStringRepository;

    @Cacheable(value = "locale-strings", key = "{#locale,#stringType}")
    public String retrieveLocalString(LocaleStringType stringType, LocaleType locale) {
        LocaleType localeType = Objects.isNull(locale) ? DEFAULT_LOCALE : locale;
        log.info("Retrieve local string has started. stringType: {}, requested_locale: {}, locale: {}", stringType, locale, localeType);
        return localeStringRepository.findByStringTypeAndLocaleAndPassiveIdIsNull(stringType, localeType)
                .map(LocaleString::getValue)
                .orElseThrow(NotFoundException::new);
    }
}
