var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var myCacheName = "web";

module.exports = {

    about: function (cb) {
        myCache.get(myCacheName + "about", function (err, value) {
            if (err)
                return cb(err, null);
            else
                if (value == undefined)
                    _about(function (err, readValue) {
                        if (err)
                            return cb(err, null);
                        else
                            myCache.set(myCacheName + "about", readValue, function (err, success) {
                                if (err)
                                    return cb(err, null);
                                if (success)
                                    return cb(null, readValue);
                                else
                                    return cb('cache internal failure', null);
                            });
                    });
                else
                    return cb(null, value);
        });
    },

    termsConditions: function (cb) {
        myCache.get(myCacheName + "termsConditions", function (err, value) {
            if (err)
                return cb(err, null);
            else
                if (value == undefined)
                    _termsConditions(function (err, readValue) {
                        if (err)
                            return cb(err, null);
                        else
                            myCache.set(myCacheName + "termsConditions", readValue, function (err, success) {
                                if (err)
                                    return cb(err, null);
                                if (success)
                                    return cb(null, readValue);
                                else
                                    return cb('cache internal failure', null);
                            });
                    });
                else
                    return cb(null, value);
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