var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var catDAL = require("../dal/categories");
var model = require("../models/category");

module.exports = {

    all: function (cb) {
        myCache.get("allCategories", function (err, value) {
            if (err)
                return cb(err, null);
            else
                if (value == undefined)
                    _all(function (err, readAll) {
                        if (err)
                            return cb(err, null);
                        else
                            myCache.set("allCategories", readAll, function (err, success) {
                                if (err)
                                    return cb(err, null);
                                if (success)
                                    return cb(null, readAll);
                                else
                                    return cb('cache internal failure', null);
                            });
                    });
                else
                    return cb(null, value);
        });
    }
}

function _all(cb) {
    try {
        catDAL.all(function (err, data) {
            if (err && (err.hasOwnProperty('id')))
                return cb(err, null);
            var cats = [];
            for (var item in data) {
                var cat = model.create();
                cat.update(data[item]);
                cats.push(cat.toJSON());
            }
            return cb(null, cats);
        });
    } catch (err) {
        return cb(err, null);
    };
}