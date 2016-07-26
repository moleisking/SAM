var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min

module.exports = {

    about: function (cb) {
        myCache.get("about", function (err, value) {
            if (err)
                return cb(err, null);
            else {
                if (value == undefined)
                    _about(function (err, readValue) {
                        if (err)
                            return cb(err, null);
                        else {
                            myCache.set("about", readValue, function (err, success) {
                                if (err)
                                    return cb(err, null);
                                if (success)
                                    return cb(null, readValue);
                                else
                                    return cb('cache internal failure', null);
                            });
                        }
                    });
                else
                    return cb(null, value);
            }
        });
    },

}

function _about(cb) {
    try {
        return cb(null, "About text is here.");
    } catch (err) {
        return cb(err, null);
    };
}