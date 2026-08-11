import {getToken, type Messaging} from "firebase/messaging";
import {VAPID_PUBLIC_KEY} from "@/util/firebaseConfig";

/**
 * Resolves the FCM registration token for this device, bound to the app's single
 * service worker registration (see src/sw.ts). Throws when the SW never becomes
 * ready, the VAPID key is missing, or FCM rejects the request — callers decide
 * between retrying and giving up.
 */
export const getFirebaseNotificationToken = async (messaging: Messaging): Promise<string | undefined> => {
    const serviceWorkerRegistration = await navigator.serviceWorker.ready;
    return getToken(messaging, {
        vapidKey: VAPID_PUBLIC_KEY,
        serviceWorkerRegistration
    });
};
