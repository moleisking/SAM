var JsonDB = require('node-json-db');
var db = new JsonDB("tagsdb", true, false);
var _path = "/tags";
var config = require("../config/settings");
var mongoClient = require('mongodb').MongoClient;
var mongoObjectId = require('mongodb').ObjectID;
var mongoUri = config.database_address;
var fs = require('fs')

module.exports = {

    all: function (cb) {
        console.log("dal tag all called");
        try {
            var data = db.getData(_path);
            console.log("tag dal dat");
            console.log(data);
            return cb(null, data);
        } catch (err) {
            console.log("tag dal err");
            console.log(err);
            return cb(err, null);
        }
    },
}
