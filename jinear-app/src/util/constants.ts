export const __DEV__ = !import.meta.env.MODE || import.meta.env.MODE === "development";

export const ROUTE_IF_LOGGED_IN = '/';

export const TERMS = "https://jinear.co/terms";
// Runtime-configurable via env. The unplugin replaces this at build time with
// a placeholder; `import-meta-env -x .env.production` substitutes the real value
// at container start. Trailing slash is normalized so callers don't have to.
export const HOST = import.meta.env.VITE_HOST ?? (__DEV__ ? "http://localhost:5173" : "https://jinear.co");

const DEV_API_ROOT = 'http://localhost:8085';
const PROD_API_ROOT = 'https://api.jinear.co';
const EXTERNAL_API_ROOT = import.meta.env.VITE_API_URL;
export const API_ROOT = (EXTERNAL_API_ROOT || (__DEV__ ? DEV_API_ROOT : PROD_API_ROOT))
    .replace(/\/+$/, "") + "/";

export const PADDLE_VENDOR_ID = __DEV__ ? 5713 : 145466;
export const PADDLE_CATALOG = {
    business_daily: {sandbox: 63817, prod: -1, price: "$3.30"},
    business_monthly: {sandbox: 63716, prod: 848738, price: "$24.90"},
    business_yearly: {sandbox: 63717, prod: 848737, price: "$249"}
};

// Apple Sign In Configuration
export const APPLE_CLIENT_ID = import.meta.env.VITE_APPLE_CLIENT_ID;//"co.jinear.app.web"
export const APPLE_REDIRECT_URI = import.meta.env.VITE_APPLE_REDIRECT_URI;//"https://jinear.co"
