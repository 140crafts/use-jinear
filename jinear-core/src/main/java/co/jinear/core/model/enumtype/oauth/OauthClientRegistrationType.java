package co.jinear.core.model.enumtype.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * How an OAuth client became known to this authorization server.
 * <p>
 * DCR clients are created by a POST to the registration endpoint (RFC 7591).
 * CIMD clients are not stored ahead of time: the client_id is an https URL that
 * dereferences to the client's own metadata document, and a row is written the
 * first time we resolve it so that the consent screen and the management UI have
 * something to display.
 * STATIC clients are pre-registered by an instance operator.
 */
@Getter
@AllArgsConstructor
public enum OauthClientRegistrationType {

    DCR(0),
    CIMD(1),
    STATIC(2);

    private final int value;
}
