var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
var helpers = require('./helpers');

module.exports = {
  entry: "./src/app/main.ts",
  resolve: {
    extensions: ["", ".js", ".ts", ".html", ".css"]
  },
  module: {
    loaders: [
      { test: /\.ts$/, loaders: ["ts", "angular2-template-loader"] },
      { test: /\.html$/, loader: "html" },
      { test: /\.(png|jpe?g|gif|svg|woff|woff2|ttf|eot|ico)$/, loader: "file?name=images/[name].[hash].[ext]" },
      { test: /\.css$/, exclude: helpers.root("src", "app"), loaders: [ExtractTextPlugin.extract('style', 'css-loader'), 'to-string', 'css'] },
      { test: /\.css$/, include: helpers.root("src", "app"), loader: "raw" }
    ]
  },
  plugins: [
    new CopyWebpackPlugin([{
      from: 'src/images',
      to: 'images'
    }]),
    new CopyWebpackPlugin([{
      from: helpers.root('localization'),
      to: 'localization'
    }]),
    new HtmlWebpackPlugin({
      template: './src/index.html',
      chunksSortMode: 'dependency'
    })
  ]
};