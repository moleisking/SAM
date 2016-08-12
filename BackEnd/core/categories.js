var NodeCache = require("node-cache");
var async = require('async');
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var catDAL = require("../dal/categories");
var modelCat = require("../models/category");
var modelTag = require("../models/tag");

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
            var cats = [], c = 0;
            async.forEach(data, function (item, callback) {
                var cat = modelCat.create();
                cat.update(item);
                cats.push(cat.toJSON());
                var tags = [];
                async.forEach(item.tags, function (itemT, callback) {
                    var tag = modelTag.create();
                    tag.update(itemT);
                    tags.push(tag.toJSON());
                    callback();
                }, function (err) {
                    if (err) { return cb(err, null); }
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