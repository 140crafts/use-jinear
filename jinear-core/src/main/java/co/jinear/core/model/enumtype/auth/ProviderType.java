package co.jinear.core.model.enumtype.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderType {
    OAUTH_MAIL,
    OTP_MAIL,
    PASSWORD_MAIL,
    SIGN_IN_WITH_APPLE,
    SINGLE_USE_LOGIN_TOKEN,
    /** A session opened by an MCP client through the OAuth consent flow. */
    MCP;
}
