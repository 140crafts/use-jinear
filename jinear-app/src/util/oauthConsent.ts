
const STORAGE_KEY = "oauth-pending-consent-request-id";

export const rememberPendingConsentRequestId = (requestId: string) => {
    try {
        sessionStorage.setItem(STORAGE_KEY, requestId);
    } catch {
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
    }
};

export const consentRoute = (requestId: string) =>
    `/oauth/consent?request_id=${encodeURIComponent(requestId)}`;
