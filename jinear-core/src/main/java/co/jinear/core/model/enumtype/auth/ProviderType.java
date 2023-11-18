package co.jinear.core.model.enumtype.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderType {
    OAUTH_MAIL,
    OTP_MAIL,
    PASSWORD_MAIL;
}
