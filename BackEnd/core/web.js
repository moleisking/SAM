var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var myCacheName = "web";
var util = require('../core/util');
var Localize = require("localize");
var myLocals = new Localize("localizations");

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

    termsConditions: function (locale, cb) {
        util.translate(myLocals, locale);
        myCache.get(myCacheName + "termsConditions" + locale, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _termsConditions(function (err, readValue) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "termsConditions" + locale, readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb("cache internal failure", null);
                });
            });
        });
    },

    cookiePolicy: function (locale, cb) {
        util.translate(myLocals, locale);
        myCache.get(myCacheName + "cookiePolicy" + locale, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _cookiePolicy(function (err, readValue) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "cookiePolicy" + locale, readValue, function (err, success) {
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
        return cb(null, myLocals.translate(myLocals.strings.termsconditions));
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
