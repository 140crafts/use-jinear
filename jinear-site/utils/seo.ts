import type { Metadata } from "next";
import { SITE_URL } from "@/utils/constants";

// Next's metadata resolution REPLACES an object wholesale rather than deep-merging
// it: a page that declares its own `openGraph` loses the root layout's og:image,
// and a page that declares its own `alternates` loses the RSS `types` entry. Every
// page therefore builds its metadata through this helper instead of hand-rolling a
// partial object and silently dropping the inherited bits.

export const OG_IMAGE = {
  url: "/og.png",
  width: 1200,
  height: 630,
  alt: "Jinear, open-source, self-hostable project management",
};

const TWITTER_HANDLE = "@usejinear";

interface BuildMetadataInput {
  /** Raw title. `title.template` in app/layout.tsx appends " — Jinear" everywhere
   *  except app/page.tsx (Next skips the segment declaring the template), so aim
   *  for 50–60 chars once that suffix is accounted for. */
  title: string;
  /** 120–160 chars. Shorter gets flagged as thin, longer gets truncated. */
  description: string;
  /** Site-relative path WITH a trailing slash, so the canonical matches the
   *  sitemap and the trailing-slash redirect in the Caddyfile. */
  path: string;
  /** Social-card overrides; default to `title` / `description`. */
  ogTitle?: string;
  ogDescription?: string;
  /** Extra Open Graph fields (article dates, authors, tags, ...). */
  openGraph?: Metadata["openGraph"];
}

export function buildMetadata({
  title,
  description,
  path,
  ogTitle,
  ogDescription,
  openGraph,
}: BuildMetadataInput): Metadata {
  const socialTitle = ogTitle ?? title;
  const socialDescription = ogDescription ?? description;

  return {
    title,
    description,
    alternates: {
      canonical: path,
      types: { "application/rss+xml": "/blog/rss.xml" },
    },
    openGraph: {
      type: "website",
      siteName: "Jinear",
      url: `${SITE_URL}${path}`,
      title: socialTitle,
      description: socialDescription,
      images: [OG_IMAGE],
      ...openGraph,
    },
    twitter: {
      card: "summary_large_image",
      site: TWITTER_HANDLE,
      creator: TWITTER_HANDLE,
      title: socialTitle,
      description: socialDescription,
      images: [OG_IMAGE.url],
    },
  };
}
