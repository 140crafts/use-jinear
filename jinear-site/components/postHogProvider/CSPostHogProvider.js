"use client";
import posthog from "posthog-js";
import { PostHogProvider } from "posthog-js/react";

// Marketing site analytics. Key/host are hardcoded (public, build-time) — this
// is a static export so there's no runtime env injection. `defaults` opts into
// PostHog's current behaviour, which auto-captures pageviews on SPA history
// changes, so no manual pageview component is needed.
const POSTHOG_KEY = "phc_kFLXOGPjchL9wfIDUqxVyMlsTOEIUV4eUO7SPbc9xI5";
const POSTHOG_HOST = "https://us.i.posthog.com";

if (typeof window !== "undefined" && !posthog.__loaded) {
  posthog.init(POSTHOG_KEY, {
    api_host: POSTHOG_HOST,
    defaults: "2026-01-30",
    person_profiles: "identified_only",
  });
}

export function CSPostHogProvider({ children }) {
  return <PostHogProvider client={posthog}>{children}</PostHogProvider>;
}
