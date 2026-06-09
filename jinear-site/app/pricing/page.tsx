import { Metadata } from "next";
import { SITE_URL } from "@/utils/constants";
import PricingClient from "./PricingClient";

export const metadata: Metadata = {
  title: "Pricing",
  description:
    "Self-host Jinear for free (AGPL-3.0), or use the hosted plan at $24.90/month — flat, no per-user pricing. Compare against Slack, Dropbox, and Asana.",
  alternates: { canonical: "/pricing" },
  openGraph: {
    type: "website",
    title: "Jinear Pricing",
    description:
      "Self-host for free or use the hosted plan at $24.90/month — flat, no per-user pricing.",
    url: `${SITE_URL}/pricing`,
  },
};

const productJsonLd = {
  "@context": "https://schema.org",
  "@type": "Product",
  name: "Jinear",
  description:
    "Open-source, self-hostable project management and calendar suite. Self-host for free or use the hosted plan.",
  brand: { "@type": "Brand", name: "Jinear" },
  offers: [
    {
      "@type": "Offer",
      name: "Self-Hosted",
      price: "0",
      priceCurrency: "USD",
      description: "Run the full suite on your own server, AGPL-3.0 licensed.",
      url: `${SITE_URL}/pricing`,
    },
    {
      "@type": "Offer",
      name: "Smarter (Hosted)",
      price: "24.90",
      priceCurrency: "USD",
      description: "Hosted plan with flat monthly pricing — no per-user fees.",
      url: `${SITE_URL}/pricing`,
    },
  ],
};

export default function PricingPage() {
  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(productJsonLd) }}
      />
      <PricingClient />
    </>
  );
}
