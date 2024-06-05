import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'


import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
proxy: {
      '/api': {
        target: 'http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      }
    },
    port: 8080,
  }
})

