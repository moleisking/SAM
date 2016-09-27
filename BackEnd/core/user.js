var model = require("../models/user");
var modelProfile = require("../models/profile");
var userDAL = require("../dal/user");
var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var jwt = require('jwt-simple');
var config = require('../config/settings');
var dist = require('./calcdist');
var toURLString = require('speakingurl');
var myCacheName = "user";

module.exports = {

    create: function (data, cb) {
        var user = model.create();
        user.update(data);
        user.nameurl(toURLString(data.name));
HEAD
        user.pass(data.pass);
        user.email(data.email);
        user.regLat(data.regLat);
        user.regLng(data.regLng);
        user.curLat(data.curLat);
        user.curLng(data.curLng);
        user.category(data.category);
        user.tags(data.tags);
        user.address(data.address);
        user.mobile(data.mobile);
        user.description("");
cf0ce7352c2e5006fb82f2c5b7ac63e394911b99
        user.image("");
        user.dayRate(0);
        user.hourRate(0);
        user.credit(0);
        user.validate().then(function () {
            if (!user.isValid)
                return cb(user.errors, null);
            user.pass(model.generateHash(user.pass()));
            userDAL.create(user.email(), user.toJSON(), function (err, data) {
                if (err)
                    return cb(err, null);
                myCache.del(myCacheName + "all");
                return cb(null, data);
            });
        }).catch(function (err) {
            return cb(err, null);
        });
    },

    read: function (email, cb) {
        if (email === null || email === undefined)
            return cb("Must provide a valid username.", null);
        myCache.get(myCacheName + "readUser" + email, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _read(email, function (err, readValue) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "readUser" + email, readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb('cache internal failure', null);
                });
            });
        });
    },

    delete: function (email, cb) {
        if (email === null || email === undefined)
            return cb("Must provide a valid value.", null);
        _delete(email, function (err, value) {
            if (err)
                return cb(err, null);
            myCache.del(myCacheName + "getNameByEmail" + email);
            myCache.del(myCacheName + "readUser" + email);
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

    getEmailFromTokenUser: function (headers, cb) {
        var token = _getToken(headers);
        if (!token)
            return null;
        var decodedUser = jwt.decode(token, config.secret);
        return decodedUser.email;
    },

    saveProfile: function (email, data, cb) {
        userDAL.read(email, function (err, userData) {
            if (err)
                return cb(err, null);
            var user = model.create();
            user.update(userData);
            user.update(data);
            user.validate().then(function () {
                if (!user.isValid)
                    return cb(user.errors, null);
                userDAL.create(email, user.toJSON(), function (err, data) {
                    if (err)
                        return cb(err, null);
                    myCache.del(myCacheName + "readMyProfile" + email);
                    myCache.del(myCacheName + "readUserProfile" + data.nameurl);
                    myCache.del(myCacheName + "all");
                    return cb(null, data);
                });
            }).catch(function (err) {
                return cb(err, null);
            });
        });
    },

    getProfile: function (nameurl, cb) {
        if (nameurl === null || nameurl === undefined)
            return cb("Must provide a valid name.", null);
        myCache.get(myCacheName + "readUserProfile" + nameurl, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            module.exports.all(function (err, data) {
                if (err)
                    return cb(err, null);
                var found = false;
                data.forEach(function (item) {
                    if (item.nameurl === nameurl) {
                        found = true;
                        _read(item.email, function (err, readValue) {
                            if (err)
                                return cb(err, null);
                            var profile = modelProfile.create();
                            profile.update(readValue);
                            myCache.set(myCacheName + "readUserProfile" + nameurl, profile.toJSON(), function (err, success) {
                                if (err)
                                    return cb(err, null);
                                if (success)
                                    return cb(null, readValue);
                                return cb('cache internal failure', null);
                            });
                        });
                    }
                });
                if (!found)
                    return cb('Profile not found', null);
            });
        });
    },

    getMyProfile: function (email, cb) {
        if (email === null || email === undefined)
            return cb("Must provide a valid name.", null);
        myCache.get(myCacheName + "readMyProfile" + email, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _readProfile(email, function (err, readValue) {
                if (err)
                    return cb(err, null);
                myCache.set(myCacheName + "readMyProfile" + email, readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb('cache internal failure', null);
                });
            });
        });
    },

    search: function (data, cb) {
        var cachename = myCacheName + "search" + data.category + data.radius + data.regLat.toString() + data.regLng.toString();
        myCache.get(cachename, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            module.exports.all(function (err, readAll) {
                if (err)
                    return cb(err, null);
                var result = readAll.filter(function (item) {
                    if (item.category === data.category && (dist.CalcDist(item, data) < parseInt(data.radius)))
                        return item;
                });
                myCache.set(cachename, result, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, result);
                    return cb('cache internal failure', null);
                });
            });
        });
    },

    getNameByEmail: function (email, cb) {
        var cachename = myCacheName + "getNameByEmail" + email;
        myCache.get(cachename, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _read(email, function (err, readValue) {
                if (err)
                    return cb(err, null);
                readValue = readValue.name;
                myCache.set(myCacheName + "getNameByEmail" + email, readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb('cache internal failure', null);
                });
            });
        });
    },

    getEmailByNameUrl: function (nameUrl, cb) {
        var cachename = myCacheName + "getEmailByNameUrl" + nameUrl;
        myCache.get(cachename, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _all(function (err, readValue) {
                if (err)
                    return cb(err, null);
                readValue.forEach(function (element) {
                    if (element.nameurl == nameUrl)
                        myCache.set(myCacheName + "getEmailByNameUrl" + nameUrl, element.email, function (err, success) {
                            if (err)
                                return cb(err, null);
                            if (success)
                                return cb(null, element.email);
                            return cb('cache internal failure', null);
                        });
                }, this);
            });
        });
    }
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

function _read(email, cb) {
    try {
        userDAL.read(email, function (err, data) {
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

function _readProfile(email, cb) {
    try {
        userDAL.read(email, function (err, data) {
            if (err)
                return cb(err, null);
            var m = modelProfile.create();
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
            if (err && err.id != 5)
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
