var model = require("../models/user");
var modelProfile = require("../models/profile");
var userDAL = require("../dal/user");
var NodeCache = require("node-cache");
var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
var jwt = require("jwt-simple");
var config = require("../config/settings");
var configMail = require("../config/settingsmail");
var dist = require("./util");
var toURLString = require("speakingurl");
var myCacheName = "user";
var emailer = require("./emailer");
var util = require("./util");
var Localize = require("localize");
var myLocals = new Localize("localizations/user");

module.exports = {

    create: function (data, locale, cb) {
        util.translate(myLocals, locale);
        var user = model.create();
        user.update(data);
        user.nameurl(toURLString(data.username));
        user.curLat(data.regLat);
        user.curLng(data.regLng);
        user.description("");
        user.image("");
        user.dayRate(0);
        user.hourRate(0);
        user.credit(100);
        user.rating(0);
        user.looking(true);
        user.guid(getGuid());
        user.timeStamp(new Date().getTime());
        user.validate().then(function () {
            if (!user.isValid)
                return cb(user.errors, null);
            user.pass(model.generateHash(user.pass()));
            userDAL.create(user.email(), user.toJSON(), function (err, data) {
                if (err)
                    return cb(err, null);
                myCache.del(myCacheName + "all");
                emailer.email(configMail.fromText, configMail.from, data.email,
                    data.name + " " + data.surname + ", " + myLocals.translate("welcome to SAM") + ".<br />" +
                    myLocals.translate("To activate your email, please enter this code in the activation area in your dashboard: ") +
                    user.guid(), myLocals.translate("Welcome to SAM."),
                    function (err, status, body, headers) {
                        if (err)
                            return cb(err, null);
                        return cb(null, data);
                    });
            });
        }).catch(function (err) {
            return cb(err, null);
        });
    },

    read: function (email, cb) {
        // WARNING: can"t add locale here by now.
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
                    return cb("cache internal failure", null);
                });
            });
        });
    },

    // delete: function (email, cb) {
    //     if (email === null || email === undefined)
    //         return cb("Must provide a valid value.", null);
    //     _delete(email, function (err, value) {
    //         if (err)
    //             return cb(err, null);
    //         myCache.del(myCacheName + "getUsernameByEmail" + email);
    //         myCache.del(myCacheName + "readUser" + email);
    //         myCache.del(myCacheName + "all");
    //         return cb(null, value);
    //     });
    // },

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
                    return cb("cache internal failure", null);
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

    saveProfile: function (email, data, locale, cb) {
        util.translate(myLocals, locale);
        if (email === null)
            return cb(myLocals.translate("Must provide a valid email."), null);
        userDAL.read(email, function (err, userData) {
            if (err)
                return cb(err, null);
            var user = model.create();
            user.update(userData);
            if (data.looking === "true")
                data.looking = true;
            else
                data.looking = false;
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

    getProfile: function (nameurl, locale, cb) {
        util.translate(myLocals, locale);
        if (nameurl === null || nameurl === undefined)
            return cb(myLocals.translate("Must provide a valid name."), null);
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
                            delete readValue.pass;
                            var profile = modelProfile.create();
                            profile.update(readValue);
                            myCache.set(myCacheName + "readUserProfile" + nameurl, profile.toJSON(), function (err, success) {
                                if (err)
                                    return cb(err, null);
                                if (success)
                                    return cb(null, readValue);
                                return cb("cache internal failure", null);
                            });
                        });
                    }
                });
                if (!found)
                    return cb(myLocals.translate("Profile not found"), null);
            });
        });
    },

    getMyProfile: function (email, locale, cb) {
        util.translate(myLocals, locale);
        if (email === null || email === undefined)
            return cb(myLocals.translate("Must provide a valid name."), null);
        myCache.get(myCacheName + "readMyProfile" + email, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _readProfile(email, function (err, readValue) {
                if (err)
                    return cb(err, null);
                if (readValue.looking === "true" || readValue.looking === true)
                    readValue.looking === true
                else
                    readValue.looking === false
                myCache.set(myCacheName + "readMyProfile" + email, readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb("cache internal failure", null);
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
                var result = readAll.filter(function (user) {
                    if (user.category === data.category && parseFloat(user.credit) > 0 && user.looking &&
                        ((dist.CalcDist(user.regLat, user.regLng, data) < parseInt(data.radius)) ||
                            (dist.CalcDist(user.curLat, user.curLng, data) < parseInt(data.radius)))
                    ) {
                        delete user.pass;
                        return user;
                    }
                });
                myCache.set(cachename, result, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, result);
                    return cb("cache internal failure", null);
                });
            });
        });
    },

    getUsernameByEmail: function (email, cb) {
        var cachename = myCacheName + "getUsernameByEmail" + email;
        myCache.get(cachename, function (err, value) {
            if (err)
                return cb(err, null);
            if (value != undefined)
                return cb(null, value);
            _read(email, function (err, readValue) {
                if (err)
                    return cb(err, null);
                readValue = readValue.username;
                myCache.set(myCacheName + "getUsernameByEmail" + email, readValue, function (err, success) {
                    if (err)
                        return cb(err, null);
                    if (success)
                        return cb(null, readValue);
                    return cb("cache internal failure", null);
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
                            return cb("cache internal failure", null);
                        });
                }, this);
            });
        });
    },

    addCredit: function (email, credit, locale, cb) {
        util.translate(myLocals, locale);
        if (email === null || email === undefined || credit === null || credit === undefined || credit < 0)
            return cb(myLocals.translate("Must provide a value and an email."), null);
        userDAL.read(email, function (err, userData) {
            if (err)
                return cb(err, null);
            var user = model.create();
            user.update(userData);
            user.credit(parseFloat(user.credit()) + parseFloat(credit));
            user.validate().then(function () {
                if (!user.isValid)
                    return cb(user.errors, null);
                userDAL.create(email, user.toJSON(), function (err, data) {
                    if (err)
                        return cb(err, null);
                    myCache.del(myCacheName + "readMyProfile" + email);
                    myCache.del(myCacheName + "readUserProfile" + data.nameurl);
                    myCache.del(myCacheName + "all");
                    emailer.email(configMail.fromText, configMail.from, email,
                        myLocalize.translate("Congrats, you have added $[1] euros to your credit. ", credit) +
                        myLocalize.translate("Now you final credit balance is: $[1] euros.",
                            parseFloat(user.credit()) + parseFloat(credit)) +
                        myLocalize.translate("You have added new credit!"),
                        function (err, status, body, headers) {
                            if (err)
                                return cb(err, null);
                            return cb(null, data);
                        });
                });
            }).catch(function (err) {
                return cb(err, null);
            });
        });
    },

    activate: function (email, code, locale, cb) {
        util.translate(myLocals, locale);
        if (email === null || email === undefined || code === null || code === undefined)
            return cb(myLocalize.translate("Must provide a code and an email."), null);
        userDAL.read(email, function (err, userData) {
            if (err)
                return cb(err, null);
            var user = model.create();
            user.update(userData);
            if (user.guid() !== code)
                return cb(myLocalize.translate("Code is not correct."), null);
            user.activated(true);
            user.validate().then(function () {
                if (!user.isValid)
                    return cb(user.errors, null);
                userDAL.create(email, user.toJSON(), function (err, data) {
                    if (err)
                        return cb(err, null);
                    myCache.del(myCacheName + "readMyProfile" + email);
                    myCache.del(myCacheName + "readUserProfile" + data.nameurl);
                    myCache.del(myCacheName + "all");
                    emailer.email(configMail.fromText, configMail.from, email,
                        myLocalize.translate("Congrats, your account is been activated. "),
                        myLocalize.translate("Your account is activated!"),
                        function (err, status, body, headers) {
                            if (err)
                                return cb(err, null);
                            return cb(null, data);
                        });
                });
            }).catch(function (err) {
                return cb(err, null);
            });
        });
    },

    resendCode: function (email, locale, cb) {
        util.translate(myLocals, locale);
        userDAL.read(email, function (err, data) {
            if (err)
                return cb(err, null);
            emailer.email(configMail.fromText, configMail.from, data.email,
                data.name + " " + data.surname + ", " + myLocals.translate("welcome to SAM") + ".<br />" +
                myLocals.translate("To activate your email, please enter this code in the activation area in your dashboard: ")
                + data.guid, myLocals.translate("Welcome to SAM."),
                function (err, status, body, headers) {
                    if (err)
                        return cb(err, null);
                    return cb(null, data);
                });
        });
    }
}

function _getToken(headers) {
    if (!headers || !headers.authorization)
        return null;
    var parted = headers.authorization.split(" ");
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

// function _delete(id, cb) {
//     try {
//         userDAL.delete(id, function (err, data) {
//             if (err)
//                 return cb(err, null);
//             return cb(null, data);
//         });
//     } catch (err) {
//         return cb(err, null);
//     };
// }

function getGuid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }
    return s4() + s4() + "-" + s4() + "-" + s4() + "-" +
        s4() + "-" + s4() + s4() + s4();
}
