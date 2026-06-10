import { Metadata } from "next";
import TermsClient from "./TermsClient";

export const metadata: Metadata = {
  title: "Terms & Conditions — Jinear",
  description:
    "Terms of service for Jinear, the self-hostable project management tool.",
};

export default function TermsPage() {
  return <TermsClient />;
}
