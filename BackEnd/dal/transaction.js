var JsonDB = require('node-json-db');
var jsondb = new JsonDB("transactionsdb", true, false);
var _path = "/transactions";
var config = require("../config/settings");

var mongoClient = require('mongodb').MongoClient;
var mongoObjectId = require('mongodb').ObjectID;
var mongoUri = config.database_address;
//var assert = require('assert');

module.exports = {

    create: function (data, cb) {
        if (config.database_type == "nodedb") {
            console.log("nodedb create transactions");
            try {
                jsondb.push(_path + "/", data, true);
                return cb(null, data);
            }
            catch (error) {
                return cb(error, null);
            }
        }
        else if (config.database_type == "mongodb") {
            console.log("monogodb create transactions");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) throw err;
                    console.log(data);

                    db.collection('transactions').insert(data, function (err, result) {
                        assert.equal(err, null);
                        console.log("transaction inserted");
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
      
            console.log("mongodb read transactions");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) throw err;

                    db.collection('transactions').find({ id: id }).toArray(function (err, data) {
                        console.log("transaction found");
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

    },

    all: function (cb) {
       
            console.log("mongodb all transaction");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) throw err;

                    db.collection('transactions').find().toArray(function (err, data) {
                        assert.equal(err, null);
                        console.log("transaction found");
                        console.dir(data);
                        return cb(null, data);
                    });
                });
            }
            catch (err) {
                console.log(err);
                return cb(err, null);
            }
        
    },

    // delete: function (usernameurl, cb) {
    //     try {
    //         var data = db.delete(_path + "/" + usernameurl);
    //         return cb(null, true);
    //     } catch (err) {
    //         return cb(err, null);
    //     }
    // },

}