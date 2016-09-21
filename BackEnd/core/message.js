var model = require("../models/message");
var user = require("./user");
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
                myCache.del(myCacheName + "allLasts" + from);
                myCache.del(myCacheName + "allWith" + from + data.to);
                return cb(null, data);
            });
        }).catch(function (err) {
            return cb(err, null);
        });
    },

    readAllLasts: function (id, cb) {
        if (id === null || id === undefined)
            return cb("Must provide a valid id.", null);
        myCache.get(myCacheName + "allLasts" + id, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _read(function (err, messages) {
                if (err)
                    return cb(err, null);
                var mines = messages.filter(function (item) {
                    if (item.from === id || item.to === id)
                        return item;
                });
                var result = [];
                mines.map(function (item) {
                    var contact = item.to === id ? item.from : item.to;
                    user.getNameByEmail(contact, function (err, data) {
                        item.name = data;
                        if (result.length === 0)
                            result.push(item);
                        else
                            for (var i = 0; i <= result.length - 1; i++)
                                if (result[i].from === contact || result[i].to === contact)
                                    if (result[i].datestamp < item.datestamp) {
                                        result.splice(i, 1);
                                        result.push(item);
                                        break;
                                    }
                    });
                });
                myCache.set(myCacheName + "allLasts" + id, result, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, result);
                    return cb('cache internal failure', null);
                });
            });
        });
    },

    readWith: function (id, to, cb) {
        if (id === null || id === undefined)
            return cb("Must provide a valid id.", null);
        myCache.get(myCacheName + "allWith" + id + to, function (err, value) {
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
                myCache.set(myCacheName + "allWith" + id + to, result, function (err, success) {
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
            if (err && err.id != 5)
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