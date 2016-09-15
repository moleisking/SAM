var model = require("../models/message");
var messageDAL = require("../dal/message");
var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var myCacheName = "message";

module.exports = {

    create: function (from, data, cb) {
        var message = model.create();
        message.from(from);
        message.to(data.to);
        message.text(data.text);
        message.datestamp(new Date().getTime());
        message.validate().then(function () {
            if (!message.isValid)
                return cb(message.errors, null);
            messageDAL.create(message.toJSON(), function (err, data) {
                if (err)
                    return cb(err, null);
                myCache.del(myCacheName + "all");
                return cb(null, data);
            });
        }).catch(function (err) {
            return cb(err, null);
        });
    },

    read: function (id, cb) {
        if (id === null || id === undefined)
            return cb("Must provide a valid id.", null);
        myCache.get(myCacheName + "all", function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _read(function (err, messages) {
                if (err)
                    return cb(err, null);
                var result = messages.filter(function (item) {
                    if (item.from === id || item.to === id)
                        return item;
                });
                myCache.set(myCacheName + "all", result, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, result);
                    return cb('cache internal failure', null);
                });
            });
        });
    },
}

function _read(cb) {
    try {
        messageDAL.read(function (err, data) {
            if (err)
                return cb(err, null);
            var messages = [];
            for (var item in data) {
                var m = model.create();
                m.update(data[item]);
                messages.push(m.toJSON());
            }
            return cb(null, messages);
        });
    } catch (err) {
        return cb(err, null);
    };
}