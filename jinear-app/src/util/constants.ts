export const __DEV__ = !import.meta.env.MODE || import.meta.env.MODE === "development";

export const ROUTE_IF_LOGGED_IN = '/';

// Runtime-configurable via env. The unplugin replaces this at build time with
// a placeholder; `import-meta-env -x .env.production` substitutes the real value
// at container start. Trailing slash is normalized so callers don't have to.
export const API_ROOT = (import.meta.env.VITE_API_URL ?? "").replace(/\/+$/, "") + "/";