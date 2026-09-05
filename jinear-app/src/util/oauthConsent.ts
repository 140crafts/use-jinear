/**
 * Remembers the connection request a signed out visitor arrived with.
 * <p>
 * A user reaching the consent screen from Claude or ChatGPT may not have a session yet.
 * Every sign in method ends on the app root: the password form navigates there, and the
 * Google and Apple flows leave the app entirely and come back. So the request id is
 * parked here and picked up again on the way back, rather than passed through a redirect
 * parameter that only the password form could carry.
 * <p>
 * sessionStorage, not localStorage, so an abandoned request cannot resurface days later
 * in a different session.
 */

const STORAGE_KEY = "oauth-pending-consent-request-id";

export const rememberPendingConsentRequestId = (requestId: string) => {
    try {
        sessionStorage.setItem(STORAGE_KEY, requestId);
    } catch {
        // Private mode or blocked site data. The user simply lands on the app root
        // after signing in and reconnects from their client.
    }
};

export const readPendingConsentRequestId = (): string | null => {
    try {
        return sessionStorage.getItem(STORAGE_KEY);
    } catch {
        return null;
    }
};

export const forgetPendingConsentRequestId = () => {
    try {
        sessionStorage.removeItem(STORAGE_KEY);
    } catch {
        // Nothing to clear.
    }
};

export const consentRoute = (requestId: string) =>
    `/oauth/consent?request_id=${encodeURIComponent(requestId)}`;
