import { fileURLToPath, URL } from 'node:url'
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  // 加载环境变量
  const env = loadEnv(mode, process.cwd(), '')
  
  // 根据环境确定后端地址
  const backendUrl = mode === 'development' 
    ? (env.VITE_API_BASE_URL || 'http://localhost:8080')
    : '' // 生产环境使用相对路径

  return {
    plugins: [
      vue(),
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    server: {
      host: '0.0.0.0', // 允许局域网访问
      port: 5173,      // 指定端口
      proxy: backendUrl ? {
        '/api': {
          target: backendUrl,
          changeOrigin: true
        },
        '/uploads': {
          target: backendUrl,
          changeOrigin: true
        }
      } : undefined
    },
    build: {
      rollupOptions: {
        output: {
          // 为不同类型的文件添加hash，确保缓存更新
          entryFileNames: 'assets/[name].[hash].js',
          chunkFileNames: 'assets/[name].[hash].js',
          assetFileNames: 'assets/[name].[hash].[ext]'
        }
      }
    }
  }
}) 