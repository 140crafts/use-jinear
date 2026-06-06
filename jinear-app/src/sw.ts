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
//
// Runtime-configurable Firebase config. The SW can't read the app bundle's
// `globalThis.import_meta_env` (that's bootstrapped in index.html, which a
// service worker never loads), so it carries its own copy of the import-meta-env
// placeholder. The docker entrypoint's `import-meta-env` pass scans dist/**/*.js
// — this compiled sw.js included — and rewrites the placeholder into the real
// process env at container start, so self-hosters point push at their own
// Firebase project without rebuilding. Keys are declared in .env.example.
//
// In dev there is no substitution step, so we read import.meta.env directly
// (Vite resolves it for the SW). The `import.meta.env.DEV` gate is statically
// false in a production build, so that whole branch — and the build-time env it
// would otherwise bake into sw.js — is dropped, leaving only the placeholder.
const runtimeEnv: Record<string, string | undefined> = import.meta.env.DEV
    ? (import.meta.env as unknown as Record<string, string | undefined>)
    : (JSON.parse('"import_meta_env_placeholder"') as Record<string, string | undefined>);

const firebaseConfig = {
    apiKey: runtimeEnv.VITE_FIREBASE_API_KEY,
    authDomain: runtimeEnv.VITE_FIREBASE_AUTH_DOMAIN,
    projectId: runtimeEnv.VITE_FIREBASE_PROJECT_ID,
    storageBucket: runtimeEnv.VITE_FIREBASE_STORAGE_BUCKET,
    messagingSenderId: runtimeEnv.VITE_FIREBASE_MESSAGING_SENDER_ID,
    appId: runtimeEnv.VITE_FIREBASE_APP_ID,
    measurementId: runtimeEnv.VITE_FIREBASE_MEASUREMENT_ID,
};

// Only wire up push when Firebase is actually configured. Self-hosters who don't
// set the VITE_FIREBASE_* env vars still get a working SW (offline shell, asset
// caching); they just don't get web push.
if (firebaseConfig.apiKey && firebaseConfig.projectId && firebaseConfig.messagingSenderId) {
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
}

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
