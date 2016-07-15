var model = require("../models/user")
var userDAL = require("../dal/user")
var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var jwt = require('jwt-simple');
var config = require('../config/settings');

module.exports = {

    create: function (data, cb) {
        var user = model.create();
        user.update(data);
        user.validate().then(function () {
            if (user.isValid) {
                user.pass(model.generateHash(user.pass()));
                userDAL.create(user.name(), user.toJSON(), function (err, data) {
                    if (err)
                        return cb(err, null);
                    myCache.del("all");
                    return cb(null, data);
                });
            } else
                return cb(user.errors, null);
        }).catch(function (err) {
            return cb(err, null);
        });
    },

    read: function (id, cb) {
        myCache.get("readUser" + id, function (err, value) {
            if (err)
                return cb(err, null);
            else {
                if (value == undefined)
                    _read(id, function (err, readValue) {
                        if (err)
                            return cb(err, null);
                        else {
                            myCache.set("readUser" + id, readValue, function (err, success) {
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

    delete: function (id, cb) {
        _delete(id, function (err, value) {
            if (err)
                return cb(err, null);
            else {
                myCache.del("readUser" + id);
                myCache.del("all");
                return cb(null, value);
            }
        });
    },

    all: function (cb) {
        myCache.get("all", function (err, value) {
            if (err)
                return cb(err, null);
            else {
                if (value == undefined)
                    _all(function (err, readAll) {
                        if (err)
                            return cb(err, null);
                        else {
                            myCache.set("all", readAll, function (err, success) {
                                if (err)
                                    return cb(err, null);
                                if (success)
                                    return cb(null, readAll);
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

    getNameFromTokenUser: function (headers, cb) {
        var token = _getNameFromToken(headers);
        if (token) {
            var decoded = jwt.decode(token, config.secret);
            return decoded.name;
        } else
            return 'UserName';
    }
}

function _getNameFromToken(headers) {
    if (headers && headers.authorization) {
        var parted = headers.authorization.split(' ');
        if (parted.length === 2)
            return parted[1];
        else
            return null;
    } else
        return null;
};

function _read(id, cb) {
    try {
        userDAL.read(id, function (err, data) {
            if (err)
                return cb(err, null);
            else {
                var user = model.create();
                user.update(data);
                return cb(null, user.toJSON());
            }
        });
    } catch (err) {
        return cb(err, null);
    };
}

function _all(cb) {
    try {
        userDAL.all(function (err, data) {
            if (err && (err.hasOwnProperty('id')))
                return cb(err, null);
            var users = [];
            for (var item in data) {
                var user = model.create();
                user.update(data[item]);
                users.push(user.toJSON());
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
            else
                return cb(null, data);
        });
    } catch (err) {
        return cb(err, null);
    };
}