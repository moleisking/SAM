var JsonDB = require('node-json-db');
var jsondb = new JsonDB("SAM", true, false);
var _path = "/products";
var config = require("../config/settings");

var mongoClient = require('mongodb').MongoClient;
var mongoObjectId = require('mongodb').ObjectID;
var mongoUri = config.database_address; //"mongodb://192.168.1.100:27017/test"; ///dbname -u dbuser -p dbpassword
var assert = require('assert');

module.exports = {

    create: function (data, cb) {
        if (config.database_type == "nodedb") {
            console.log("nodedb create product");
            try {
                jsondb.push(_path + "/", data, true);
                return cb(null, data);
            }
            catch (error) {
                return cb(error, null);
            }
        }
        else if (config.database_type == "mongodb") {
            console.log("monogodb create product");
            try
            {
                mongoClient.connect(mongoUri, function(err, db) {               
                    if (err) throw err;   
                    console.log(data);
                
                    db.collection('products').insert(data , function(err, result) {
                        assert.equal(err, null);               
                        console.log("product inserted");
                        //callback(result);
                        db.close();
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
        console.log("nodedb read product");        
        if (config.database_type == "nodedb") {            
            try {                
                var data = jsondb.getData(_path + "/" + id);
                return cb(null, data);
            } catch (err) {               
                return cb(err, null);
            }
        }
        else if (config.database_type == "mongodb") {
            console.log("mongodb read product");           
            try
            {
                mongoClient.connect(mongoUri, function (err, db) {                 
                    if (err) throw err;   
                       
                    db.collection('products').find({ id: id }).toArray(function (err, data) {
                        console.log("product found");  
                        assert.equal(err, null);                       
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

    all: function (cb) {
        if (config.database_type == "nodedb") {
            try {
                var data = jsondb.getData(_path);
                return cb(null, data);
            } catch (err) {
                return cb(err, null);
            }
        }
        else if (config.database_type == "mongodb") {
            console.log("mongodb all product");       
            try
            {            
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) throw err;     
                       
                    db.collection('products').find().toArray(function (err, data) {
                        assert.equal(err, null);
                        console.log("product found");
                        console.dir(data);
                        return cb(null,data);
                        });                                  
                });
            }
            catch (err)
            {               
                console.log(err);
                return cb(err, null);
            }
        }// if (config.database_type == "mongodb")
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