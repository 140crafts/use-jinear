package co.jinear.core.model.enumtype.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OauthClientRegistrationType {

    DCR(0),
    CIMD(1),
    STATIC(2);

    private final int value;
}
