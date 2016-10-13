var model = require('nodejs-model');

var profile = new model("Profile")
    .attr('name')
    .attr('nameurl')
    .attr('description')
    .attr('rating')// rating 
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
    .attr('credit')
    .attr('category')
    .attr('tags');

module.exports = profile;