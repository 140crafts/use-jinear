/// <reference lib="webworker" />
import {createHandlerBoundToURL, precacheAndRoute} from "workbox-precaching";
import {NavigationRoute, registerRoute} from "workbox-routing";
import {CacheFirst, NetworkFirst} from "workbox-strategies";
import {CacheableResponsePlugin} from "workbox-cacheable-response";
import {ExpirationPlugin} from "workbox-expiration";
import {initializeApp} from "firebase/app";
import {getMessaging, onBackgroundMessage} from "firebase/messaging/sw";

declare const self: ServiceWorkerGlobalScope;

self.skipWaiting();
self.addEventListener("activate", (event) => {
    event.waitUntil(self.clients.claim());
});

precacheAndRoute(self.__WB_MANIFEST);

// SPA shell: every navigation returns the precached index.html so the React
// app can boot offline and run against the API + asset caches.
registerRoute(
    new NavigationRoute(createHandlerBoundToURL("/index.html"), {
        denylist: [/^\/dev-sw\.js/, /^\/workbox-/, /^\/sw\.js/],
    }),
);

registerRoute(
    ({request}) => request.destination === "image" || request.destination === "font",
    new CacheFirst({
        cacheName: "asset-cache",
        plugins: [
            new ExpirationPlugin({maxEntries: 100, maxAgeSeconds: 60 * 60 * 24 * 30}),
        ],
    }),
);

// --- Firebase Cloud Messaging -------------------------------------------------
// Lives in this single SW (instead of a separate public/firebase-messaging-sw.js)
// so there is one registration. The app passes this SW's registration to
// getToken(..., {serviceWorkerRegistration}) so FCM binds push to it.
const firebaseConfig = {
    apiKey: "AIzaSyBZq8Pg2pDDweDSNqTwdrCR-xBe1mJGBco",
    authDomain: "jinear-f3ab4.firebaseapp.com",
    projectId: "jinear-f3ab4",
    storageBucket: "jinear-f3ab4.appspot.com",
    messagingSenderId: "72155538781",
    appId: "1:72155538781:web:767cb1558cd358cfacf4b4",
    measurementId: "G-FMXGQ5XM95",
};

const messaging = getMessaging(initializeApp(firebaseConfig));

// Combined (notification + data) payloads are auto-displayed by the FCM SDK while
// the page is backgrounded, so we only render manually for data-only messages —
// otherwise the user would see two notifications.
onBackgroundMessage(messaging, (payload) => {
    if (payload.notification) {
        return;
    }
    const title = payload.data?.title ?? "Jinear";
    self.registration.showNotification(title, {
        body: payload.data?.body,
        icon: "https://jinear.co/icons/notification-icon.png",
        data: {launchUrl: payload.data?.launchUrl},
    });
});

// Clicking a notification focuses an existing tab (and routes it to launchUrl) or
// opens a new one. Auto-displayed FCM notifications nest data under FCM_MSG.
self.addEventListener("notificationclick", (event) => {
    event.notification.close();
    const data = (event.notification.data ?? {}) as Record<string, any>;
    const launchUrl: string = data.launchUrl ?? data?.FCM_MSG?.data?.launchUrl ?? "/";
    event.waitUntil(
        (async () => {
            const clients = await self.clients.matchAll({type: "window", includeUncontrolled: true});
            for (const client of clients) {
                if ("focus" in client) {
                    await client.focus();
                    if (launchUrl && "navigate" in client) {
                        try {
                            await (client as WindowClient).navigate(launchUrl);
                        } catch {
                            // cross-origin / disallowed navigation — ignore
                        }
                    }
                    return;
                }
            }
            if (launchUrl) {
                await self.clients.openWindow(launchUrl);
            }
        })(),
    );
});
