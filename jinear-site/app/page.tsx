import { Metadata } from "next";
import { SITE_URL } from "@/utils/constants";
import HomeClient from "./HomeClient";

export const metadata: Metadata = {
  title: "Jinear — Self-Hosted Project Management & Calendar | Open Source",
  description:
    "Jinear is an open-source, self-hostable project management and calendar app for indie developers and small teams. AGPL-3.0, Docker Compose install, no per-user pricing.",
  alternates: { canonical: "/" },
};

const IMG_BASE = "https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2";

const softwareApplicationJsonLd = {
  "@context": "https://schema.org",
  "@type": "SoftwareApplication",
  name: "Jinear",
  operatingSystem: "Web, Self-hosted",
  applicationCategory: "BusinessApplication",
  url: SITE_URL,
  image: [
    `${IMG_BASE}/projects-dark.png`,
    `${IMG_BASE}/chat-dark.png`,
    `${IMG_BASE}/files-2-dark.png`,
    `${IMG_BASE}/calendar-dark.png`,
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
