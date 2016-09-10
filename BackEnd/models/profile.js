var model = require('nodejs-model');

var profile = new model("Profile")
    .attr('name')
    .attr('description')
    .attr('address')
    .attr('image')
    .attr('mobile');

module.exports = profile;