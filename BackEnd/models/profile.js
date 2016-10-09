var model = require('nodejs-model');

var profile = new model("Profile")
    .attr('name')
    .attr('nameurl')
    .attr('description')
    .attr('score')// rating 
    .attr('address')
    .attr('email')
    .attr('image')
    .attr('hourRate')
    .attr('dayRate')
    .attr('regLng')
    .attr('regLat')
    .attr('curLat')
    .attr('curLng')
    .attr('mobile')
    .attr('category')
    .attr('tags');

module.exports = profile;