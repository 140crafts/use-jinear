import { Metadata } from "next";
import HomeClient from "./HomeClient";

export const metadata: Metadata = {
  title: "Jinear — Self-Hosted Project Management & Calendar | Open Source",
  description:
    "Jinear is an open-source, self-hostable project management and calendar app for indie developers and small teams. AGPL-3.0, Docker Compose install, no per-user pricing.",
};

export default function HomePage() {
  return <HomeClient />;
}
