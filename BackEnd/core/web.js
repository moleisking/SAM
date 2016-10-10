var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var myCacheName = "web";

module.exports = {

    about: function (cb) {
        myCache.get(myCacheName + "about", function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _about(function (err, readValue) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "about", readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb("cache internal failure", null);
                });
            });
        });
    },

    termsConditions: function (cb) {
        myCache.get(myCacheName + "termsConditions", function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _termsConditions(function (err, readValue) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "termsConditions", readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb("cache internal failure", null);
                });
            });
        });
    },

    cookiePolicy: function (cb) {
        myCache.get(myCacheName + "cookiePolicy", function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _cookiePolicy(function (err, readValue) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "cookiePolicy", readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb("cache internal failure", null);
                });
            });
        });
    },
}

function _about(cb) {
    try {
        return cb(null, "About text is here from backend.");
    } catch (err) {
        return cb(err, null);
    };
}

function _termsConditions(cb) {
    try {
        return cb(null, "Terms & conditions text is here from backend.");
    } catch (err) {
        return cb(err, null);
    };
}

function _cookiePolicy(cb) {
    try {
        return cb(null, "Cookie policy text is here from backend.");
    } catch (err) {
        return cb(err, null);
    };
}
