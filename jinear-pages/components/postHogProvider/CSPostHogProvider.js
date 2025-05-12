"use client";
import posthog from "posthog-js";
import { PostHogProvider } from "posthog-js/react";
import { env } from "next-runtime-env";
import Logger from "@/utils/logger";

const logger = Logger("CSPostHogProvider");

if (typeof window !== "undefined") {
  const NEXT_PUBLIC_POSTHOG_KEY = env("NEXT_PUBLIC_POSTHOG_KEY");
  const NEXT_PUBLIC_POSTHOG_HOST = env("NEXT_PUBLIC_POSTHOG_HOST");
  if (NEXT_PUBLIC_POSTHOG_KEY && NEXT_PUBLIC_POSTHOG_HOST) {
    logger.log(`Initializing postHog. NEXT_PUBLIC_POSTHOG_KEY: ${NEXT_PUBLIC_POSTHOG_KEY}, NEXT_PUBLIC_POSTHOG_HOST: ${NEXT_PUBLIC_POSTHOG_HOST}`);
    posthog.init(NEXT_PUBLIC_POSTHOG_KEY, {
      api_host: NEXT_PUBLIC_POSTHOG_HOST,
      person_profiles: "identified_only",
      capture_pageview: false,
      capture_pageleave: true
    });
  }
}

export function CSPostHogProvider({ children }) {
  return <PostHogProvider client={posthog}>{children}</PostHogProvider>;
}