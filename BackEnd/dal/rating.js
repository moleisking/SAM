var JsonDB = require("node-json-db");
var db = new JsonDB("Rating", true, false);
var _path = "/rating/";

module.exports = {

    create: function (id, data, cb) {
        try {
            db.push(_path + id + "[]", data, true);
            var read = db.getData(_path + id);
            var x = 0;
            read.forEach(function (element) {
                if (element.from === data.from)
                    x++;
            }, this);
            var found = x === 1;
            x = 0;
            read.forEach(function (element) {
                if (element.from === data.from && !found) {
                    db.delete(_path + id + "[" + x + "]");
                    found = true;
                }
                else
                    x++;
            }, this);
            return cb(null, data);
        } catch (error) {
            return cb(error, null);
        }
    },

    read: function (id, cb) {
        try {
            var data = db.getData(_path + id);
            return cb(null, data);
        } catch (err) {
            return cb(err);
        }
    },
}
