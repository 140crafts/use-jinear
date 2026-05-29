/// <reference lib="webworker" />
/**
 * Jinear service worker (compiled by Serwist to public/sw.js).
 *
 * Goal: a revisiting user opens the app instantly, even on a slow or dropped
 * connection. We do NOT cache API responses here — RTK Query + redux-persist
 * own that data and handle refetch-on-reconnect. The SW only owns the static
 * shell: documents, JS/CSS, fonts and images.
 *
 * Firebase push notifications run inside this same worker via the importScripts
 * below (see public/firebase-messaging-sw.js).
 */
import {
  CacheFirst,
  ExpirationPlugin,
  NetworkFirst,
  NetworkOnly,
  Serwist,
  StaleWhileRevalidate
} from "serwist";
import type { PrecacheEntry, RuntimeCaching, SerwistGlobalConfig } from "serwist";

declare global {
  interface WorkerGlobalScope extends SerwistGlobalConfig {
    // Build-time precache manifest injected by @serwist/next.
    __SW_MANIFEST: (PrecacheEntry | string)[] | undefined;
  }
}

declare const self: ServiceWorkerGlobalScope;

// Firebase Cloud Messaging background handler. Adds its own push /
// notificationclick listeners; does not conflict with Serwist's fetch handler.
self.importScripts("/firebase-messaging-sw.js");

/**
 * Requests that must always go to the network and never be cached by the SW.
 * Jinear API (api.jinear.co / staging.api.jinear.co, all under /v1/) and the
 * realtime message service are owned by the app layer, not the SW.
 */
const isApiRequest = (url: URL): boolean =>
  url.hostname.includes("api.jinear.co") ||
  url.hostname === "message.jinear.co" ||
  url.pathname.startsWith("/v1/");

/**
 * Remote media (uploads, avatars, attachments) served from our storage hosts.
 */
const isRemoteMedia = (url: URL): boolean =>
  url.hostname === "files.jinear.co" || url.hostname === "storage.googleapis.com";

// First match wins, so the API rule is intentionally first.
const runtimeCaching: RuntimeCaching[] = [
  {
    // API + realtime: leave entirely to redux-persist.
    matcher: ({ url }) => isApiRequest(url),
    handler: new NetworkOnly()
  },
  {
    // Page documents: try network briefly, fall back to the cached shell so a
    // slow/offline revisit still renders instantly.
    matcher: ({ request }) => request.mode === "navigate",
    handler: new NetworkFirst({
      cacheName: "pages",
      networkTimeoutSeconds: 3,
      plugins: [new ExpirationPlugin({ maxEntries: 64, maxAgeSeconds: 7 * 24 * 60 * 60 })]
    })
  },
  {
    // Hashed JS/CSS build assets are immutable → serve from cache instantly.
    matcher: ({ request }) => request.destination === "script" || request.destination === "style",
    handler: new CacheFirst({
      cacheName: "static-assets",
      plugins: [new ExpirationPlugin({ maxEntries: 128, maxAgeSeconds: 30 * 24 * 60 * 60 })]
    })
  },
  {
    // Fonts rarely change → cache aggressively for a year.
    matcher: ({ request, url }) =>
      request.destination === "font" ||
      url.hostname === "fonts.gstatic.com" ||
      url.hostname === "fonts.googleapis.com",
    handler: new CacheFirst({
      cacheName: "fonts",
      plugins: [new ExpirationPlugin({ maxEntries: 32, maxAgeSeconds: 365 * 24 * 60 * 60 })]
    })
  },
  {
    // Local images (incl. /_next/image) and remote media: serve cached copy
    // immediately, refresh in the background.
    matcher: ({ request, url }) => request.destination === "image" || isRemoteMedia(url),
    handler: new StaleWhileRevalidate({
      cacheName: "images",
      plugins: [new ExpirationPlugin({ maxEntries: 256, maxAgeSeconds: 30 * 24 * 60 * 60 })]
    })
  }
];

const serwist = new Serwist({
  // Build assets are precached, so they are available offline before the first
  // request. Serwist also handles cache versioning and stale-cache cleanup.
  precacheEntries: self.__SW_MANIFEST,
  skipWaiting: true,
  clientsClaim: true,
  navigationPreload: true,
  runtimeCaching
});

serwist.addEventListeners();
