import { Metadata } from "next";
import { SITE_URL } from "@/utils/constants";
import { buildMetadata } from "@/utils/seo";
import PricingClient from "./PricingClient";
import { PRICING_FAQ } from "./faq";

export const metadata: Metadata = buildMetadata({
  title: "Pricing: Free Self-Hosting or Flat Hosted Plans",
  description:
    "Self-host Jinear for free (AGPL-3.0), or use the hosted plan at $24.90/month, flat, with no per-user pricing. Compare against Slack, Dropbox, and Asana.",
  path: "/pricing/",
  ogTitle: "Jinear Pricing",
  ogDescription:
    "Self-host for free or use the hosted plan at $24.90/month, flat, no per-user pricing.",
});

const productJsonLd = {
  "@context": "https://schema.org",
  "@type": "Product",
  name: "Jinear",
  description:
    "Open-source, self-hostable tasks, calendar, notes and file sharing suite. Self-host for free or use the hosted plan.",
  brand: { "@type": "Brand", name: "Jinear" },
  offers: [
    {
      "@type": "Offer",
      name: "Starter",
      price: "0",
      priceCurrency: "USD",
      description: "Free hosted plan for workspaces of up to 3 people. No card required.",
      url: `${SITE_URL}/pricing/`,
    },
    {
      "@type": "Offer",
      name: "Self-Hosted",
      price: "0",
      priceCurrency: "USD",
      description: "Run the full suite on your own server, AGPL-3.0 licensed.",
      url: `${SITE_URL}/pricing/`,
    },
    {
      "@type": "Offer",
      name: "Team",
      price: "24.90",
      priceCurrency: "USD",
      description: "Hosted plan with flat monthly pricing for unlimited people.",
      url: `${SITE_URL}/pricing/`,
    },
  ],
};

const faqJsonLd = {
  "@context": "https://schema.org",
  "@type": "FAQPage",
  mainEntity: PRICING_FAQ.map(({ question, answer }) => ({
    "@type": "Question",
    name: question,
    acceptedAnswer: { "@type": "Answer", text: answer },
  })),
};

export default function PricingPage() {
  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(productJsonLd) }}
      />
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(faqJsonLd) }}
      />
      <PricingClient />
    </>
  );
}
