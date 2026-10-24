import { Metadata } from "next";
import { GITHUB_URL, GITLAB_URL, SITE_URL } from "@/utils/constants";
import { buildMetadata, MAINTAINER_JSON_LD } from "@/utils/seo";
import HomeClient from "./HomeClient";

export const metadata: Metadata = buildMetadata({
  title: "Jinear, Open-Source Self-Hosted Task Manager & Calendar",
  description:
    "Open-source, self-hostable tasks, calendar, notes and file sharing for indie devs and small teams. Install with Docker Compose. No per-user pricing.",
  path: "/",
  ogDescription:
    "Self-hostable tasks, calendar, notes and file sharing. AGPL-3.0, installs with one Docker Compose command, no per-user pricing.",
});

const IMG_BASE = "https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v3";

const softwareApplicationJsonLd = {
  "@context": "https://schema.org",
  "@type": "SoftwareApplication",
  name: "Jinear",
  description:
    "Open-source, self-hostable tasks, boards, calendar, notes and file storage, with an MCP server for AI assistants. AGPL-3.0, installs with Docker Compose.",
  operatingSystem: "Web, Self-hosted",
  applicationCategory: "BusinessApplication",
  url: SITE_URL,
  license: "https://www.gnu.org/licenses/agpl-3.0.html",
  isAccessibleForFree: true,
  dateCreated: "2022-11",
  author: MAINTAINER_JSON_LD,
  sameAs: [GITHUB_URL, GITLAB_URL],
  image: [
    `${IMG_BASE}/v2.1-tasks.png`,
    `${IMG_BASE}/v2.1-calendar.png`,
    `${IMG_BASE}/v2.1-notes.png`,
    `${IMG_BASE}/v2.1-files.png`,
  ],
  offers: [
    { "@type": "Offer", price: "0", priceCurrency: "USD", name: "Self-Hosted" },
    { "@type": "Offer", price: "0", priceCurrency: "USD", name: "Starter" },
    { "@type": "Offer", price: "24.90", priceCurrency: "USD", name: "Team" },
  ],
};

export default function HomePage() {
  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(softwareApplicationJsonLd) }}
      />
      <HomeClient />
    </>
  );
}
