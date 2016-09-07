var model = require('nodejs-model');

var profile = new model("Profile")
    .attr('description')
    .attr('address')
    .attr('imageCode')
    .attr('imageBase64')
    .attr('mobile');

module.exports = profile;