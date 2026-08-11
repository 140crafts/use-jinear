import Logger from "@/util/logger";

const logger = Logger("NotificationPermission");

// Set only by the modal's "Reject" button. Suppresses the automatic prompt for
// good; the Communication preferences screen clears it so the user can opt back in.
export const NOTIFICATIONS_REJECT_KEY = "do-not-ask-notifications";

export const isNotificationSupported = () =>
    typeof window === "object" && "Notification" in window;

const canQueryPermissions = () =>
    typeof navigator === "object" && "permissions" in navigator && typeof navigator.permissions?.query === "function";

const queryNotificationStatus = async (): Promise<PermissionStatus | undefined> => {
    if (!canQueryPermissions()) {
        return undefined;
    }
    try {
        return await navigator.permissions.query({name: "notifications" as PermissionName});
    } catch (error) {
        // Safari (and older WebKit) reject "notifications" as an unsupported descriptor.
        logger.warn({message: "Permissions API query for notifications failed.", error});
        return undefined;
    }
};

const fromPermissionState = (state: PermissionState): NotificationPermission =>
    state === "prompt" ? "default" : (state as NotificationPermission);

/**
 * Authoritative read of the browser's notification permission.
 *
 * `Notification.permission` is a synchronous snapshot that browsers can serve
 * stale on a freshly loaded document — that stale "default" is what made the
 * permission modal reappear on every app open for users who had already granted.
 * The Permissions API is the spec's canonical source, so prefer it and only fall
 * back to the snapshot where the API is unavailable.
 */
export const readNotificationPermission = async (): Promise<NotificationPermission> => {
    if (!isNotificationSupported()) {
        return "denied";
    }
    const status = await queryNotificationStatus();
    return status ? fromPermissionState(status.state) : Notification.permission;
};

/**
 * Subscribes to permission changes and returns an unsubscribe. Fires when the
 * user grants/blocks from browser UI, from another tab, or when the browser
 * auto-revokes — so the app can react without waiting for the next reload.
 * A no-op unsubscribe is returned where the Permissions API is unavailable.
 */
export const onNotificationPermissionChange = (
    callback: (permission: NotificationPermission) => void
): (() => void) => {
    let status: PermissionStatus | undefined;
    let cancelled = false;

    const handleChange = () => {
        if (status) {
            callback(fromPermissionState(status.state));
        }
    };

    void queryNotificationStatus().then((result) => {
        if (cancelled || !result) {
            return;
        }
        status = result;
        status.addEventListener("change", handleChange);
    });

    return () => {
        cancelled = true;
        status?.removeEventListener("change", handleChange);
    };
};
