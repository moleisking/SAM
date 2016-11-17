var JsonDB = require("node-json-db");
var db = new JsonDB("messagesdb", true, false);
var _path = "/messages";

var config = require("../config/settings");

var mongoClient = require('mongodb').MongoClient;
var mongoObjectId = require('mongodb').ObjectID;
var mongoUri = config.database_address; //"mongodb://192.168.1.100:27017/test"; ///dbname -u dbuser -p dbpassword

module.exports = {

    create: function (data, cb) {
        if (config.database_type == "nodedb") {
            console.log("nodedb create message");
            try {
                db.push(_path + "[]", data, true);
                return cb(null, data);
            } catch (error) {
                return cb(error, null);
            }
        }
        else if (config.database_type == "mongodb") {
            console.log("mongodb create message");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) throw err;
                    console.log("Connected correctly to mongodb server");
                    console.log(data);

                    db.collection('messages').insert(data, function (err, result) {
                        assert.equal(err, null);
                        console.log("message inserted");
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

    //this should be all    
    read: function (cb) {
        if (config.database_type == "nodedb") {
            console.log("nodedb read message");
            try {
                var data = db.getData(_path);
                return cb(null, data);
            } catch (err) {
                return cb(err);
            }
        }
        else if (config.database_type == "mongodb") {
            console.log("mongodb read message");

            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) throw err;        /*{ email: email }*/
                    db.collection('messages').find().toArray(function (err, data) {
                        console.log("message found");
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
