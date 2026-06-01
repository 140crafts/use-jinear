/// <reference lib="webworker" />
import {createHandlerBoundToURL, precacheAndRoute} from "workbox-precaching";
import {NavigationRoute, registerRoute} from "workbox-routing";
import {CacheFirst, NetworkFirst} from "workbox-strategies";
import {CacheableResponsePlugin} from "workbox-cacheable-response";
import {ExpirationPlugin} from "workbox-expiration";

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
