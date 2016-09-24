var model = require('nodejs-model');

var profile = new model("Profile")
    .attr('name')
    .attr('nameurl')
    .attr('description')
    .attr('address')
    .attr('email')
    .attr('image')
    .attr('mobile');

module.exports = profile;