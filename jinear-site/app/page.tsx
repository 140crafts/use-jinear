import { Metadata } from "next";
import { SITE_URL } from "@/utils/constants";
import { buildMetadata } from "@/utils/seo";
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
  operatingSystem: "Web, Self-hosted",
  applicationCategory: "BusinessApplication",
  url: SITE_URL,
  image: [
    `${IMG_BASE}/v2.1-tasks.png`,
    `${IMG_BASE}/v2.1-calendar.png`,
    `${IMG_BASE}/v2.1-notes.png`,
    `${IMG_BASE}/v2.1-files.png`,
  ],
  offers: [
    { "@type": "Offer", price: "0", priceCurrency: "USD", name: "Self Hosted" },
    { "@type": "Offer", price: "0", priceCurrency: "USD", name: "Free" },
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
