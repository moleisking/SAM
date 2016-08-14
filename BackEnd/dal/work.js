var JsonDB = require('node-json-db');
var db = new JsonDB("works", true, false);

module.exports = {

    create: function (data, cb) {
        try {
            db.push("/" + data.username + "/" + data.nameurl, data);
            cb(null, data);
        } catch (error) {
            return cb(error, null);
        }
    },

    allByUser: function (username, cb) {
        try {
            var data = db.getData("/" + username);
            return cb(null, data);
        } catch (err) {
            return cb(err, null);
        }
    },
}
