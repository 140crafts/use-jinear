// Minimal constants for the marketing site. The full app constants live in
// jinear-app / jinear-web; here we only need a few public values.

export const __DEV__ = process.env.NODE_ENV !== "production";

// Where app CTAs (log in / sign up / open app) point. Baked at build time.
export const APP_URL = process.env.NEXT_PUBLIC_APP_URL || "https://app.jinear.co";
export const SITE_URL = process.env.NEXT_PUBLIC_SITE_URL || "https://jinear.co";

// Kept for parity with reused components.
export const ROUTE_IF_LOGGED_IN = APP_URL;
export const APP_STORE_URL = "https://apps.apple.com/app/jinear/id6504384303";
export const PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=co.jinear.app";

// External links surfaced in marketing UI.
// Pro plan price shown on /pricing (kept in sync with the app's Paddle catalog).
export const PADDLE_CATALOG = {
  business_monthly: { price: "$24.90" },
  business_yearly: { price: "$249" },
};

export const GITHUB_URL = "https://github.com/140crafts/use-jinear";
export const GITLAB_URL = "https://gitlab.com/140crafts/use-jinear";
export const SELF_HOSTING_DOCS_URL =
  "https://github.com/140crafts/use-jinear/blob/main/jinear-installation-scripts/README.md";
