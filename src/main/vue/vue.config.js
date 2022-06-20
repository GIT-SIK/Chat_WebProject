module.exports = {
  devServer: {
    port: 9091,
    proxy: 'http://localhost:9092',
    disableHostCheck: true
  }
}

const path = require('path')

module.exports = {
  outputDir: path.resolve(__dirname, '../resources/static/dist')
}
