var model = require("../models/work");
var workDAL = require("../dal/work");
var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var myCacheName = "work";
var toSEOString = require('speakingurl');

module.exports = {

    create: function (data, cb) {
        var work = model.create();
        work.name(data.name);
        work.description(data.description);
        work.category(data.category);
        work.tags(data.tags);
        work.nameUrl(toSEOString(data.name));
        work.validate().then(function () {
            if (work.isValid) {
                workDAL.create(work.nameUrl(), work.toJSON(), function (err, data) {
                    if (err)
                        return cb(err, null);
                    myCache.del(myCacheName + "all");
                    return cb(null, data);
                });
            } else
                return cb(work.errors, null);
        }).catch(function (err) {
            return cb(err, null);
        });
    },

}
