/** @type {import('next').NextConfig} */

const withPWA = require("next-pwa")({
  dest: "public",
});

const nextConfig = {
  // reactStrictMode: true,
  reactStrictMode: false,
  swcMinify: true,
  images: {
    domains: ["storage.googleapis.com"],
  },
};

module.exports = withPWA(nextConfig);
