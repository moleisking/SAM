// var model = require("../models/work");
// var workDAL = require("../dal/work");
// var NodeCache = require("node-cache");
// var myCache = new NodeCache({ stdTTL: 300, checkperiod: 310 }); //300 = 5 min
// var myCacheName = "work";
// var toURLString = require('speakingurl');

// module.exports = {

//     create: function (username, data, cb) {
//         var work = model.create();
//         work.name(data.name);
//         work.username(username);
//         work.description(data.description);
//         work.category(data.category);
//         work.tags(data.tags);
//         work.nameurl(toURLString(data.name));
//         work.validate().then(function () {
//             if (work.isValid) {
//                 workDAL.create(work.toJSON(), function (err, data) {
//                     if (err)
//                         return cb(err, null);
//                     myCache.del(myCacheName + "allByUser" + data.username);
//                     return cb(null, data);
//                 });
//             } else
//                 return cb(work.errors, null);
//         }).catch(function (err) {
//             return cb(err, null);
//         });
//     },

//     allByUser: function (username, cb) {
//         if (username === null || username === undefined)
//             return cb("Must provide a valid name.", null);
//         myCache.get(myCacheName + "allByUser" + username, function (err, value) {
//             if (err)
//                 return cb(err, null);
//             else
//                 if (value == undefined)
//                     _allByUser(username, function (err, data) {
//                         if (err)
//                             return cb(err, null);
//                         else
//                             myCache.set(myCacheName + "allByUser" + username, data, function (err, success) {
//                                 if (err)
//                                     return cb(err, null);
//                                 if (success)
//                                     return cb(null, data);
//                                 else
//                                     return cb('cache internal failure', null);
//                             });
//                     });
//                 else
//                     return cb(null, value);
//         });
//     },

//     read: function (username, id, cb) {
//         if (id === null || id === undefined)
//             return cb("Must provide a valid id.", null);
//         myCache.get(myCacheName + "readWork" + id, function (err, value) {
//             if (err)
//                 return cb(err, null);
//             else
//                 if (value == undefined)
//                     _read(username, id, function (err, readValue) {
//                         if (err)
//                             return cb(err, null);
//                         else
//                             myCache.set(myCacheName + "readWork" + id, readValue, function (err, success) {
//                                 if (err)
//                                     return cb(err, null);
//                                 if (success)
//                                     return cb(null, readValue);
//                                 else
//                                     return cb('cache internal failure', null);
//                             });
//                     });
//                 else
//                     return cb(null, value);
//         });
//     },
// }

// function _allByUser(username, cb) {
//     try {
//         workDAL.allByUser(username, function (err, data) {
//             if (err && err.hasOwnProperty('id') && err.id != 5)
//                 return cb(err, null);
//             else {
//                 if (data == null)
//                     return cb("No works created yet", null);
//                 var works = [];
//                 for (var item in data) {
//                     var m = model.create();
//                     m.update(data[item]);
//                     works.push(m.toJSON());
//                 }
//                 return cb(null, works);
//             }
//         });
//     } catch (err) {
//         return cb(err, null);
//     };
// }

// function _read(username, id, cb) {
//     try {
//         workDAL.read(username, id, function (err, data) {
//             if (err)
//                 return cb(err, null);
//             else {
//                 var m = model.create();
//                 m.update(data);
//                 return cb(null, m.toJSON());
//             }
//         });
//     } catch (err) {
//         return cb(err, null);
//     };
// }