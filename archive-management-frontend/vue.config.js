module.exports = {
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  },
  lintOnSave: false,
  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          'primary-color': '#0075de',
          'link-color': '#0075de',
          'border-radius-base': '4px',
          'font-family': "Inter, -apple-system, system-ui, 'Segoe UI', Helvetica, Arial, sans-serif"
        },
        javascriptEnabled: true
      }
    }
  }
}
