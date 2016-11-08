var NodeCache = require("node-cache");
var async = require("async");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var myCacheName = "categories";
var catDAL = require("../dal/categories");
var modelCat = require("../models/category");
var modelTag = require("../models/tag");

module.exports = {

    all: function (locale, cb) {
        myCache.get(myCacheName + "allCategories" + locale, function (err, value) {
            if (err)
                return cb(err, null);
            else
                if (value == undefined)
                    _all(locale, function (err, readAll) {
                        if (err)
                            return cb(err, null);
                        else
                            myCache.set(myCacheName + "allCategories" + locale, readAll, function (err, success) {
                                if (err)
                                    return cb(err, null);
                                if (success)
                                    return cb(null, readAll);
                                else
                                    return cb("cache internal failure", null);
                            });
                    });
                else
                    return cb(null, value);
        });
    },
}

function _all(locale, cb) {
    try {
        catDAL.all(function (err, data) {
            if (err && (err.hasOwnProperty("id")))
                return cb(err, null);
            var cats = [], c = 0;
            async.forEach(data, function (item, callback) {
                console.log(locale)
                var cat = modelCat.create();
                cat.id(item.id);
                cat.name(item[locale].name);
                cat.description(item[locale].description);
                cats.push(cat.toJSON());
                var tags = [];
                async.forEach(item[locale].tags, function (itemT, callback) {
                    var tag = modelTag.create();
                    tag.update(itemT);
                    tags.push(tag.toJSON());
                    callback();
                }, function (err) {
                    if (err)
                        return cb(err, null);
                    console.log("Processing all tags completed of " + item.name);
                    cats[c].tags = tags;
                });
                c++;
                callback();
            }, function (err) {
                if (err) { return cb(err, null); }
                console.log("Processing all categories completed");
                return cb(null, cats);
            });
        });
    } catch (err) {
        return cb(err, null);
    };
}
