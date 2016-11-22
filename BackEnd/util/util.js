var JwtStrategy = require("passport-jwt").Strategy;
var ExtractJwt = require("passport-jwt").ExtractJwt;

module.exports = {

    CalcDist: function (userLat, userLng, placeMap) {
        var m = module.exports;
        var R = 6371; // km

        var dLat = m.toRad(parseFloat(placeMap.regLat) - parseFloat(userLat));
        var dLon = m.toRad(parseFloat(placeMap.regLng) - parseFloat(userLng));
        var lat1 = m.toRad(parseFloat(userLat));
        var lat2 = m.toRad(parseFloat(placeMap.regLat));

        /*var dLat = m.toRad(parseFloat(placeMap.lat) - parseFloat(user.currentLat));
        var dLon = m.toRad(parseFloat(placeMap.lng) - parseFloat(user.currentLng));
        var lat1 = m.toRad(parseFloat(user.currentLat));
        var lat2 = m.toRad(parseFloat(placeMap.lat));*/

        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        var d = R * c;
        return d;
    },

    toRad: function (value) {
        return value * Math.PI / 180;
    },

    translate: function (locals, lang) {
        if (lang === undefined || lang === "" || lang.length > 2)
            lang = "en";
        locals.setLocale(lang);
    },

    email: function (from_name, from_email, to_email, body, subject, cb) {
        var helper = require("sendgrid").mail;
        var from_email = new helper.Email(from_email, from_name);
        var to_email = new helper.Email(to_email);
        var content = new helper.Content("text/html", body);
        var mail = new helper.Mail(from_email, subject, to_email, content);

        var sg = require("sendgrid")(config.mail_provider_key);               
        var request = sg.emptyRequest({
            method: "POST",
            path: "/v3/mail/send",
            body: mail.toJSON(),
        });

        sg.API(request, function (err, response) {
            console.log(response.statusCode);
            console.log(response.body);
            console.log(response.headers);
            return cb(err, response.statusCode, response.body, response.headers);
        });
    },

    getEmailFromTokenUser: function (headers, cb) {        
        var token = _getToken(headers);
        if (!token)
            return null;
        var decodedUser = jwt.decode(token, config.secret);
        return decodedUser.email;
    },
    
    /*getPassport: function (passport) {  
        var opts = {};
        opts.jwtFromRequest = ExtractJwt.fromAuthHeader();
        opts.secretOrKey = config.secret;
        passport.use(new JwtStrategy(opts, function (jwt_payload, done) {
            user.read(jwt_payload.email, function (err, data) {
                if (err)
                    return done(err, false);
                if (data)
                    done(null, data);
                else
                    done(null, false);
            });
        }));
     },*/
     
}

function _getToken(headers) {
    if (!headers || !headers.authorization)
        return null;
    var parted = headers.authorization.split(" ");
    if (parted.length === 2)
        return parted[1];
    else
        return null;
};