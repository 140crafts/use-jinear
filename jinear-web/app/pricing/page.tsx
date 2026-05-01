import { Metadata } from "next";
import PricingClient from "./PricingClient";

export const metadata: Metadata = {
  title: "Pricing — Jinear | Flat Fee, Unlimited Users, Free Self-Hosted",
  description:
    "Free for individuals, $24.90/month flat for unlimited team members, or self-host for free. No per-user fees, ever.",
};

export default function PricingPage() {
  return <PricingClient />;
}
