import { useLocation } from "react-router-dom";
import { useEffect } from "react";
import { usePostHog } from "posthog-js/react";

export default function PostHogPageView(): null {
  const { pathname, search } = useLocation();
  const posthog = usePostHog();

  useEffect(() => {
    // Track pageviews
    if (pathname && posthog) {
      let url = window.origin + pathname;
      if (search) {
        url = url + search;
      }
      posthog.capture("$pageview", {
        $current_url: url,
      });
    }
  }, [pathname, search, posthog]);

  return null;
}