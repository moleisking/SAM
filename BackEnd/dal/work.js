var JsonDB = require('node-json-db');
var db = new JsonDB("works", true, false);
var _path = "/work";

module.exports = {

    create: function (workName, data, cb) {
        try {
            db.push(_path + "/" + workName, data);
            cb(null, data);
        } catch (error) {
            return cb(error, null);
        }
    },
}
