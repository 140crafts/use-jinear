package co.jinear.core.repository;

import co.jinear.core.model.entity.LocaleString;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocaleStringRepository extends JpaRepository<LocaleString, Integer> {

    Optional<LocaleString> findByStringTypeAndLocaleAndPassiveIdIsNull(LocaleStringType stringType, LocaleType locale);
}