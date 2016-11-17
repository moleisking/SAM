var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var myCacheName = "categories";
var catDAL = require("../dal/categories");
var model = require("../models/category");

module.exports = {

    all: function (locale, cb) {
        myCache.get(myCacheName + "allCategories" + locale, function (err, value) {
            if (err)
                return cb(err, null);
            else {
                if (value != undefined)
                    return cb(null, value);
                _all(locale, function (err, readAll) {
                    if (err)
                        return cb(err, null);
                    myCache.set(myCacheName + "allCategories" + locale, readAll, function (err, success) {
                        if (err)
                            return cb(err, null);
                        if (success)
                            return cb(null, readAll);
                        return cb("cache internal failure", null);
                    });
                });
            }
        });
    },
}

function _all(locale, cb) {
    try {
        catDAL.all(function (err, data) {
            if (err && err.hasOwnProperty("id"))
                return cb(err, null);
            var cats = [];
            data.forEach(function (item) {
                var cat = model.create();
                cat.id(item.id);
                cat.name(item[locale].name);
                cat.description(item[locale].description);
                cats.push(cat.toJSON());
            }, this);
            return cb(null, cats);
        });
    } catch (err) {
        return cb(err, null);
    };
}
