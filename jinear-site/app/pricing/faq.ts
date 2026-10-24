import { PADDLE_CATALOG } from "@/utils/constants";

const PRO_PRICE = PADDLE_CATALOG.business_monthly.price;

// Rendered on /pricing and repeated as FAQPage JSON-LD, so the two never drift apart.
export const PRICING_FAQ = [
  {
    question: "Is it really one developer?",
    answer:
      "Yes. Jinear is built and maintained by one person, in the open. That's also why the pricing is this simple. There's no sales team to feed.",
  },
  {
    question: "What happens if I self-host?",
    answer:
      "You get the full app for free under the AGPL-3.0 license. Bring it up with a single Docker Compose file and point it at your domain. Your data never touches my servers. The installer asks whether the instance may send a daily update check and an anonymous usage report, and you can say no to both.",
  },
  {
    question: "Does the flat price change with team size?",
    answer: `No. ${PRO_PRICE}/mo covers unlimited people. A team of 4 and a team of 40 pay exactly the same.`,
  },
];
