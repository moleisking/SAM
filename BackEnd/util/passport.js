var JwtStrategy = require("passport-jwt").Strategy;
var ExtractJwt = require("passport-jwt").ExtractJwt;
var user = require("../dal/user");
var config = require("../config/settings");
//var passport = require('passport'); //scott added this

module.exports = function (passport) {
    
    var opts = {};
    opts.jwtFromRequest = ExtractJwt.fromAuthHeader();
    opts.secretOrKey = config.secret;
    //opts.issuer = "accounts.examplesoft.com";
    //opts.audience = "yoursite.net";
    passport.use(new JwtStrategy(opts, function (jwt_payload, done) {
        user.read(jwt_payload.email, function (err, data) {
            console.log("passport user read"); 
            console.log(jwt_payload.email);   
            if (err)
            {
                console.log("passport_err:" + err);
                return done(err, false);
            }
            if (data)
            {
                console.log("passport_data_success:" + data);
                done(null, data);
            }                
            else
            {
                console.log("passport_else_err:" + err);
                done(null, false);
            }
                
        });
    }));
};
