var JsonDB = require('node-json-db');
var db = new JsonDB("categories", true, false);
var _path = "/categories";

module.exports = {

    all: function (cb) {
        try {
            var data = db.getData(_path);
            return cb(null, data);
        } catch (err) {
            return cb(err, null);
        }
    },
}
