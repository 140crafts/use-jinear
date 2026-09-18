package co.jinear.core.converter.oauth;

import co.jinear.core.model.enumtype.oauth.OauthClientRegistrationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class OauthClientRegistrationTypeConverter implements AttributeConverter<OauthClientRegistrationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OauthClientRegistrationType type) {
        return Optional.ofNullable(type).map(OauthClientRegistrationType::getValue).orElse(null);
    }

    @Override
    public OauthClientRegistrationType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(OauthClientRegistrationType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
