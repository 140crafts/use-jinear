package co.jinear.core.model.enumtype.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderType {
    OTP_MAIL,
    PASSWORD_MAIL;
}
