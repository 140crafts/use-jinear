const { withAxiom } = require("next-axiom");
const { withSentryConfig } = require("@sentry/nextjs");

/** @type {import("next").NextConfig} */

const withPWA = require("next-pwa")({
  dest: "public"
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


// module.exports = withAxiom(withSentryConfig(withPWA(nextConfig), { silent: false }, { hideSourceMaps: true }));
module.exports = withPWA(nextConfig);
