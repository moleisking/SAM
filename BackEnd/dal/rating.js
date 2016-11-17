var JsonDB = require("node-json-db");
var db = new JsonDB("Rating", true, false);
var _path = "/rating/";
var config = require("../config/settings");

var mongoClient = require('mongodb').MongoClient;
var mongoObjectId = require('mongodb').ObjectID;
var mongoUri = config.database_address; //"mongodb://192.168.1.100:27017/test"; ///dbname -u dbuser -p dbpassword

module.exports = {

    create: function (id, data, cb) {
        if (config.database_type == "nodedb") {
            console.log("nodedb create rating");
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
        }
        else if (config.database_type == "mongodb") { 
            console.log("mongodb create rating");
            try
            {
                mongoClient.connect(mongoUri, function(err, db) {               
                    if (err) throw err;  
                    console.log(data);                
                    db.collection('ratings').insert(data , function(err, result) {
                        assert.equal(err, null);               
                        console.log("rating inserted");
                        //callback(result);
                        //db.close();
                        return cb(null, data);
                    });      
                }); 
            }
            catch (err)
            {                
                console.log(err);
                return cb(err, null);
            } 
        }     
    },

    read: function (id, cb) {
        if (config.database_type == "nodedb") {
            console.log("nodedb read rating");
            try {
                var data = db.getData(_path + id);
                return cb(null, data);
            } catch (err) {
                return cb(err);
            }
        }
        else if (config.database_type == "mongodb")
        {
            console.log("mongodb read rating");           
            try
            {
                mongoClient.connect(mongoUri, function (err, db) {   
                    if (err) throw err;     
                    db.collection('ratings').find({ id: id }).toArray(function (err, data) {
                        console.log("rate found");  
                        assert.equal(err, null);
                        console.log("Found the following records");
                        console.dir(data);
                        return cb(null,data);
                        });      
                }); //close connection
            }
            catch (err)
            {               
                console.log(err);
                return cb(err, null);
            }
            
        }//close if (config.database_type == "mongodb")     
    },
}
