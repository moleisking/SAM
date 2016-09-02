var model = require("../models/user");
var modelprofile = require("../models/profile");
var userDAL = require("../dal/user");
var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var myCacheName = "user";
var jwt = require('jwt-simple');
var config = require('../config/settings');
var toURLString = require('speakingurl');

module.exports = {

    create: function (data, cb) {
        var user = model.create();
        user.name(data.name);
        user.nameurl(toURLString(data.name));
        user.pass(data.pass);
        user.email(data.email);
        user.lat(data.lat);
        user.lng(data.lng);
        user.currentLat(data.lat);
        user.currentLng(data.lng);
        user.category(data.category);
        user.tags(data.tags);
        user.address(data.address);
        user.mobile(data.mobile);
        user.rate(0);
        user.balance(0);
        user.validate().then(function () {
            if (!user.isValid)
                return cb(user.errors, null);
            user.pass(model.generateHash(user.pass()));
            userDAL.create(user.name(), user.toJSON(), function (err, data) {
                if (err)
                    return cb(err, null);
                myCache.del(myCacheName + "all");
                var userdata = data;
                module.exports.saveProfile(user.name(), null, function (err, data) {
                    if (err)
                        return cb(err, null);
                    return cb(null, userdata);
                });
            });
        }).catch(function (err) {
            return cb(err, null);
        });
    },

    read: function (username, cb) {
        if (username === null || username === undefined)
            return cb("Must provide a valid username.", null);
        myCache.get(myCacheName + "readUser" + username, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _read(username, function (err, readValue) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "readUser" + username, readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb('cache internal failure', null);
                });
            });
        });
    },

    delete: function (id, cb) {
        if (id === null || id === undefined)
            return cb("Must provide a valid value.", null);
        _delete(id, function (err, value) {
            if (err)
                return cb(err, null);
            myCache.del(myCacheName + "readUser" + id);
            myCache.del(myCacheName + "all");
            return cb(null, value);
        });
    },

    all: function (cb) {
        myCache.get(myCacheName + "all", function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _all(function (err, readAll) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "all", readAll, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readAll);
                    return cb('cache internal failure', null);
                });
            });
        });
    },

    readByEmail: function (id, cb) {
        if (id === null || id === undefined)
            return cb("Must provide a valid value.", null);
        myCache.get(myCacheName + "readUserByEmail" + id, function (err, value) {
            if (err)
                return cb(err, null);
            if (value !== undefined)
                return cb(null, value);
            _readByEmail(id, function (err, readValue) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "readUserByEmail" + id, readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb('cache internal failure', null);
                });
            });
        });
    },

    getNameFromTokenUser: function (headers, cb) {
        var token = _getToken(headers);
        if (!token)
            return null;
        var decodedUser = jwt.decode(token, config.secret);
        return decodedUser.name;
    },

    saveProfile: function (username, data, cb) {
        var profile = modelprofile.create();
        if (data === null)
            profile.description("");
        else
            profile.description(data.description);
        profile.validate().then(function () {
            if (!profile.isValid)
                return cb(profile.errors, null);
            userDAL.saveProfile(username, profile.toJSON(), function (err, data) {
                if (err)
                    return cb(err, null);
                myCache.del(myCacheName + "readUserProfile" + username);
                return cb(null, data);
            });
        }).catch(function (err) {
            return cb(err, null);
        });
    },

    getProfile: function (name, cb) {
        if (name === null || name === undefined)
            return cb("Must provide a valid name.", null);
        myCache.get(myCacheName + "readUserProfile" + name, function (err, value) {
            if (err)
                return cb(err, null);
            if (value !== undefined)
                return cb(null, value);
            _readProfile(name, function (err, readValue) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "readUserProfile" + name, readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb('cache internal failure', null);
                });
            });
        });
    },
}

function _getToken(headers) {
    if (!headers || !headers.authorization)
        return null;
    var parted = headers.authorization.split(' ');
    if (parted.length === 2)
        return parted[1];
    else
        return null;
};

function _read(id, cb) {
    try {
        userDAL.read(id, function (err, data) {
            if (err)
                return cb(err, null);
            var m = model.create();
            m.update(data);
            return cb(null, m.toJSON());
        });
    } catch (err) {
        return cb(err, null);
    };
}

function _readProfile(id, cb) {
    try {
        userDAL.readProfile(id, function (err, data) {
            if (err)
                return cb(err, null);
            var user = modelprofile.create();
            user.update(data);
            return cb(null, user.toJSON());
        });
    } catch (err) {
        return cb(err, null);
    };
}

function _readByEmail(id, cb) {
    try {
        userDAL.readByEmail(id, function (err, data) {
            if (err)
                return cb(err, null);
            if (!data)
                return cb('User not found', null);
            var m = model.create();
            m.update(data);
            return cb(null, m.toJSON());
        });
    } catch (err) {
        return cb(err, null);
    };
}

function _all(cb) {
    try {
        userDAL.all(function (err, data) {
            if (err && err.hasOwnProperty('id'))
                return cb(err, null);
            var users = [];
            for (var item in data) {
                var m = model.create();
                m.update(data[item]);
                users.push(m.toJSON());
            }
            return cb(null, users);
        });
    } catch (err) {
        return cb(err, null);
    };
}

function _delete(id, cb) {
    try {
        userDAL.delete(id, function (err, data) {
            if (err)
                return cb(err, null);
            return cb(null, data);
        });
    } catch (err) {
        return cb(err, null);
    };
}