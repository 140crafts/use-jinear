package co.jinear.core.model.vo.auth;

/**
 * Attached to an Authentication as its details when the caller's session id is already
 * known and is not carried inside the credential.
 * <p>
 * A browser request carries its session id inside the JWT cookie, so SessionInfoService
 * reads it back out of the token. A machine caller authenticated some other way, such as
 * an MCP access token, has no such token to parse, and this is how it tells the session
 * layer which session it is acting under.
 */
public interface SessionCarrier {

    String sessionInfoId();

    /** Null when the caller has no locale of its own and the account default applies. */
    default String localeName() {
        return null;
    }
}
