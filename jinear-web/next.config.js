/** @type {import('next').NextConfig} */
const nextConfig = {
  // reactStrictMode: true,
  reactStrictMode: false,
  swcMinify: true,
  images: {
    domains: ["storage.googleapis.com"],
  },
};

module.exports = nextConfig;
