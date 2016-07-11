var JwtStrategy = require('passport-jwt').Strategy;
 
var user = require('../core/user');
var config = require('./settings');

module.exports = function (passport) {
  var opts = {};
  opts.secretOrKey = config.secret;
  passport.use(new JwtStrategy(opts, function(jwt_payload, done) {
    user.read({id: jwt_payload.id}, function(err, data) {
          if (err)
              return done(err, false);
          if (data)
              done(null, data);
          else
              done(null, false);
      });
  }));
};