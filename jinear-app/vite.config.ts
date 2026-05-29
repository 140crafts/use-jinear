import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { VitePWA } from 'vite-plugin-pwa'
import ImportMetaEnvPlugin from '@import-meta-env/unplugin'
import tsconfigPaths from 'vite-tsconfig-paths'

export default defineConfig({
  css: {
    modules: {
      localsConvention: 'camelCaseOnly',
      generateScopedName: '[name]__[local]__[hash:base64:5]',
    },
  },
  plugins: [
    react(),
    tsconfigPaths(),
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
      },
      includeAssets: ['favicon.svg', 'robots.txt', 'offline.html'],
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
        theme_color: '#ff6a00',
        background_color: '#ffffff',
        display: 'standalone',
        start_url: '/',
        icons: [
          {
            src: '/favicon.svg',
            sizes: 'any',
            type: 'image/svg+xml',
            purpose: 'any',
          },
        ],
      },
    }),
  ],
})
