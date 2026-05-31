/// <reference lib="webworker" />
import {createHandlerBoundToURL, precacheAndRoute} from "workbox-precaching";
import {NavigationRoute, registerRoute} from "workbox-routing";
import {CacheFirst, NetworkFirst} from "workbox-strategies";
import {CacheableResponsePlugin} from "workbox-cacheable-response";
import {ExpirationPlugin} from "workbox-expiration";

declare const self: ServiceWorkerGlobalScope;

// Runtime-configurable. Substituted by `@import-meta-env/cli` at container start.
// const API_URL = import.meta.env.VITE_API_URL;
const API_URL = 'http://localhost:8085';
const API_ORIGIN = API_URL ? new URL(API_URL).origin : null;

self.skipWaiting();

precacheAndRoute(self.__WB_MANIFEST);

// SPA shell: every navigation returns the precached index.html so the React
// app can boot offline and run against the API + asset caches.
registerRoute(
    new NavigationRoute(createHandlerBoundToURL("/index.html"), {
        denylist: [/^\/dev-sw\.js/, /^\/workbox-/, /^\/sw\.js/],
    }),
);

// if (API_ORIGIN) {
//     registerRoute(
//         ({url, request}) => request.method === "GET" && url.origin === API_ORIGIN,
//         new NetworkFirst({
//             cacheName: "api-cache",
//             networkTimeoutSeconds: 5,
//             plugins: [
//                 new CacheableResponsePlugin({statuses: [0, 200]}),
//                 new ExpirationPlugin({maxEntries: 200, maxAgeSeconds: 60 * 60 * 24 * 7}),
//             ],
//         }),
//     );
// }

registerRoute(
    ({request}) => request.destination === "image" || request.destination === "font",
    new CacheFirst({
        cacheName: "asset-cache",
        plugins: [
            new ExpirationPlugin({maxEntries: 100, maxAgeSeconds: 60 * 60 * 24 * 30}),
        ],
    }),
);
