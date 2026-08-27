import type {Metadata, Viewport} from "next";
import React from "react";
import {SITE_URL} from "@/utils/constants";
import {OG_IMAGE} from "@/utils/seo";
import "../styles/app.scss";
import "../styles/fonts.css";
import Root from "@/components/root/Root";
import {CSPostHogProvider} from "@/components/postHogProvider/CSPostHogProvider";

export const viewport: Viewport = {
    themeColor: "#fbfaf7",
    width: "device-width",
    initialScale: 1,
};

export const metadata: Metadata = {
    metadataBase: new URL(SITE_URL),
    title: {
        default: "Jinear, Open-Source Self-Hosted Task Manager & Calendar",
        template: "%s, Jinear",
    },
    description:
        "Jinear is an open-source, self-hostable tasks, calendar, notes and file sharing suite for indie developers and small teams. AGPL-3.0, Docker Compose install, no per-user pricing.",
    applicationName: "Jinear",
    icons: {
        icon: "/favicon.ico",
        shortcut: "/favicon.ico",
    },
    alternates: {
        canonical: "/",
        types: {
            "application/rss+xml": "/blog/rss.xml",
        },
    },
    openGraph: {
        type: "website",
        siteName: "Jinear",
        url: SITE_URL,
        title: "Jinear, Open-Source Self-Hosted Task Manager & Calendar",
        description:
            "Open-source, self-hostable tasks, calendar, notes and file sharing. AGPL-3.0, Docker Compose install, no per-user pricing.",
        images: [OG_IMAGE],
    },
    twitter: {
        card: "summary_large_image",
        site: "@usejinear",
        creator: "@usejinear",
        title: "Jinear, Open-Source Self-Hosted Task Manager & Calendar",
        description:
            "Open-source, self-hostable tasks, calendar, notes and file sharing. AGPL-3.0, Docker Compose install, no per-user pricing.",
        images: [OG_IMAGE.url],
    },
    robots: {
        index: true,
        follow: true,
    },
};

const organizationJsonLd = {
    "@context": "https://schema.org",
    "@type": "Organization",
    name: "Jinear",
    url: SITE_URL,
    logo: `${SITE_URL}/images/icon/icon-512x512.png`,
    sameAs: [
        "https://github.com/140crafts/use-jinear",
        "https://gitlab.com/140crafts/use-jinear",
        "https://twitter.com/usejinear",
    ],
};

export default function RootLayout({children}: { children: React.ReactNode }) {
    return (
        <html lang="en" className="light">
        <head>
            <script
                type="application/ld+json"
                dangerouslySetInnerHTML={{__html: JSON.stringify(organizationJsonLd)}}
            />
        </head>
        <body>
        <CSPostHogProvider>
            <Root>
                {children}
            </Root>
        </CSPostHogProvider>
        </body>
        </html>
    );
}
