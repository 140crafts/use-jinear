const { withAxiom } = require("next-axiom");
const { withSentryConfig } = require("@sentry/nextjs");

/** @type {import("next").NextConfig} */

const withPWA = require("next-pwa")({
  dest: "public"
});

const nextConfig = {
  // reactStrictMode: true,
  reactStrictMode: false,
  swcMinify: false,
  minify: false,
  compress: false,
  images: {
    domains: ["storage.googleapis.com"]
  }
};


module.exports = withAxiom(withSentryConfig(withPWA(nextConfig), { silent: false }, { hideSourcemaps: true }));
