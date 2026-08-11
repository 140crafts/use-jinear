const path = require("path");

/** @type {import("next").NextConfig} */
const nextConfig = {
  // Fully static site: `next build` emits a static `out/` directory served by Caddy.
  // `output: "export"` is only applied for production builds; `next dev` runs in
  // normal mode, which renders dynamic routes (e.g. /blog/[slug]) on demand. In
  // export mode the dev server has flaky support for dynamic-route params; the
  // static export itself still uses generateStaticParams() at build time.
  output: process.env.NODE_ENV === "development" ? undefined : "export",
  // The static export has no Image Optimization server, so serve images as-is.
  images: {
    unoptimized: true,
  },
  // Emit /path/index.html so clean URLs work behind a static file server.
  trailingSlash: true,
  reactStrictMode: true,
  // Reused SCSS modules do `@import "/styles/breakpoints.scss"`, so resolve those
  // against the project root.
  sassOptions: {
    includePaths: [path.join(__dirname), path.join(__dirname, "styles")],
  },
};

module.exports = nextConfig;
