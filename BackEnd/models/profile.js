var model = require('nodejs-model');

var profile = new model("Profile")
    .attr('description');

module.exports = profile;