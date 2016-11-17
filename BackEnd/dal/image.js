var JsonDB = require("node-json-db");
var db = new JsonDB("imagesdb", true, false);
var _path = "/images/";
var config = require("../config/settings");
var mongoClient = require('mongodb').MongoClient;
var mongoObjectId = require('mongodb').ObjectID;
var mongoUri = config.database_address;

module.exports = {

    create: function (id, data, cb) {
        if (config.database_type == "nodedb") {
            console.log("read nodedb image");
            try {
                db.push(_path + id, data, true);
                return cb(null, data);
            }
            catch (error) {
                return cb(error, null);
            }
        }
        else if (config.database_type == "mongodb") {
            console.log("read mongodb image");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) throw err;
                    console.log(data);
                    db.collection('images').insert(data, function (err, result) {
                        assert.equal(err, null);
                        console.log("image inserted");
                        //callback(result);
                        //db.close();
                        return cb(null, data);
                    });
                });
            }
            catch (err) {
                console.log(err);
                return cb(err, null);
            }
        }
    },

    read: function (id, cb) {
        if (config.database_type == "nodedb") {
            console.log("read nodedb image");
            try {
                var data = db.getData(_path + id);
                return cb(null, data);
            } catch (err) {
                return cb(err);
            }
        }
        else if (config.database_type == "mongodb") {
            console.log("read mongodb image");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) throw err;
                    db.collection('images').find({ email: email }).toArray(function (err, data) {
                        console.log("image found");
                        assert.equal(err, null);
                        console.dir(data);
                        return cb(null, data);
                    });
                }); //close connection
            }
            catch (err) {
                console.log(err);
                return cb(err, null);
            }
        }//close if (config.database_type == "mongodb")     
    },
}
