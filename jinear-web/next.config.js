const { withAxiom } = require("next-axiom");
const { withSentryConfig } = require("@sentry/nextjs");

/** @type {import("next").NextConfig} */

const withSerwist = require("@serwist/next").default({
  swSrc: "app/sw.ts",
  swDest: "public/sw.js",
  // Keep dev untouched: no caching while developing.
  disable: process.env.NODE_ENV === "development",
  // Force a reload when the connection comes back so users get fresh assets.
  reloadOnOnline: true
  // `register` defaults to true → /sw.js is auto-registered on the client.
});

const nextConfig = {
  // reactStrictMode: true,
  reactStrictMode: false,
  // swcMinify: false,
  // compress: false,
  images: {
    // domains: ["storage.googleapis.com", "placehold.co", "files.jinear.co"]
    remotePatterns: [
      {
        protocol: "http",
        hostname: "**"
      },
      {
        protocol: "https",
        hostname: "**"
      }
    ]

  },
  experimental: {
    missingSuspenseWithCSRBailout: false
  }

};


// module.exports = withAxiom(withSentryConfig(withSerwist(nextConfig), { silent: false }, { hideSourceMaps: true }));
module.exports = withSerwist(nextConfig);
