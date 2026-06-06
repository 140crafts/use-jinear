import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { VitePWA } from 'vite-plugin-pwa'
import ImportMetaEnvPlugin from '@import-meta-env/unplugin'
// import tsconfigPaths from 'vite-tsconfig-paths'

export default defineConfig({
  resolve:{
    tsconfigPaths: true
  },
  css: {
    modules: {
      localsConvention: 'camelCase',
      generateScopedName: '[name]__[local]__[hash:base64:5]',
    },
  },
  plugins: [
    react(),
    // tsconfigPaths(),
    // Replaces `import.meta.env.VITE_*` with placeholders at build time.
    // At container start, `import-meta-env -x .env.production` rewrites those
    // placeholders in dist/ using the real process env. Same image, any domain.
    ImportMetaEnvPlugin.vite({ example: '.env.example' }),
    VitePWA({
      registerType: 'autoUpdate',
      // injectManifest lets us author the SW ourselves (src/sw.ts) so it can
      // read import.meta.env.* the same way the app bundle does.
      strategies: 'injectManifest',
      srcDir: 'src',
      filename: 'sw.ts',
      injectManifest: {
        globPatterns: ['**/*.{js,css,html,svg,png,ico,woff,woff2}'],
        // Minify the SW with terser instead of the default esbuild: esbuild
        // rewrites the import-meta-env placeholder's outer quote to a backtick
        // (`JSON.parse(`"..."`)`), which the `import-meta-env` CLI doesn't match,
        // so the runtime substitution of sw.js (see src/sw.ts) would silently
        // no-op. Terser keeps a quote style the CLI recognizes.
        minify: 'terser',
      },
      includeAssets: ['favicon.ico', 'robots.txt', 'offline.html'],
      devOptions: {
        enabled: true,
        type: 'module',
        navigateFallback: 'index.html',
        suppressWarnings: true,
      },
      manifest: {
        name: 'Jinear',
        short_name: 'Jinear',
        description: 'Jinear project management suite',
        theme_color: '#16171a',
        background_color: '#16171a',
        display: 'standalone',
        start_url: '/',
        icons: [
          {src: '/images/icon/icon-72x72.png', sizes: '72x72', type: 'image/png', purpose: 'any'},
          {src: '/images/icon/icon-96x96.png', sizes: '96x96', type: 'image/png', purpose: 'any'},
          {src: '/images/icon/icon-128x128.png', sizes: '128x128', type: 'image/png', purpose: 'any'},
          {src: '/images/icon/icon-144x144.png', sizes: '144x144', type: 'image/png', purpose: 'any'},
          {src: '/images/icon/icon-152x152.png', sizes: '152x152', type: 'image/png', purpose: 'any'},
          {src: '/images/icon/icon-192x192.png', sizes: '192x192', type: 'image/png', purpose: 'any'},
          {src: '/images/icon/icon-384x384.png', sizes: '384x384', type: 'image/png', purpose: 'any'},
          {src: '/images/icon/icon-512x512.png', sizes: '512x512', type: 'image/png', purpose: 'any'},
          {src: '/images/icon/maskable-icon.png', sizes: '512x512', type: 'image/png', purpose: 'maskable'},
        ],
      },
    }),
  ],
})
