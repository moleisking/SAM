var JsonDB = require('node-json-db');
var jsondb = new JsonDB("usersdb", true, false);
var _path = "/user";
var config = require("../config/settings");

var mongoClient = require('mongodb').MongoClient;
var mongoObjectId = require('mongodb').ObjectID;
var mongoUri = config.database_address; //"mongodb://192.168.1.100:27017/test"; ///dbname -u dbuser -p dbpassword
var assert = require('assert');

module.exports = {

    create: function (email, data, cb) {
        if (config.database_type == "nodedb") {
            console.log("Call -> nodedb:user:create");
            try {
                jsondb.push(_path + "/" + email, data, true);
                return cb(null, data);
            }
            catch (error) {
                return cb(error, null);
            }
        }
        else if (config.database_type == "mongodb") {
            console.log("monogodb create user");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err)  {
                         console.log("mongodb failed to connect");
                     };
                    console.log(data);

                    db.collection('users').insert(data, function (err, result) {
                        assert.equal(err, null);
                        console.log("user inserted");
                        return cb(null, data);
                        //callback(result);
                        //db.close();
                    });
                });
            }
            catch (err) {
                console.log(err);
                return cb(err, null);
            }
        }
    },

    read: function (email, cb) {
       
            console.log("mongodb read user");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) {
                         console.log("mongodb failed to connect");
                     };
                    console.log("mongodb read user query");
                    db.collection('users').find({ email: email }).toArray(function (err, data) {
                        console.log("dal user found");
                        assert.equal(err, null);
                        //console.dir(data);                        
                        return cb(null, data);
                    });

                }); //close connection
            }
            catch (err) {
                console.log("mongodb read user:" + err);
                return cb(err, null);
            }

    },

    update: function (email, data, cb) {
       
            console.log("mongodb update user");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) {
                         console.log("mongodb failed to connect");
                     };
                    console.log("mongodb read user query");
                    db.collection('users').update({ email: email }, { $set: { data } }, {}).toArray(function (err, data) {
                        console.log("dal user found");
                        assert.equal(err, null);
                        //console.dir(data);                        
                        return cb(null, data);
                    });

                }); //close connection
            }
            catch (err) {
                console.log("mongodb read user:" + err);
                return cb(err, null);
            }
    },

    updateGuid: function (guid, cb) {
       
            console.log("mongodb update user");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err) {
                         console.log("mongodb failed to connect");
                     };
                    console.log("mongodb read user query");
                    db.collection('users').updateOne({ guid: guid }, { $set: { authenticated: true } }, {}).toArray(function (err, data) {
                        console.log("dal user found");
                        assert.equal(err, null);
                        //console.dir(data);                        
                        return cb(null, data);
                    });

                }); //close connection
            }
            catch (err) {
                console.log("mongodb read user:" + err);
                return cb(err, null);
            }
    },


    all: function ( tags , cb) {
       
            console.log("mongodb all user");
            try {
                mongoClient.connect(mongoUri, function (err, db) {
                    if (err)  {
                         console.log("mongodb failed to connect");
                     };

                    db.collection('users').find({ tags: tags }).toArray(function (err, data) {
                        assert.equal(err, null);
                        console.log("user found");
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


// var JsonDB = require('node-json-db');
// //The second argument is used to tell the DB to save after each push 
// //If you put false, you'll have to call the save() method. 
// //The third argument is to ask JsonDB to save the database in an human readable format. (default false) 
// var db = new JsonDB("myDataBase", true, false);

// //Pushing the data into the database 
// //With the wanted DataPath 
// //By default the push will override the old value 
// db.push("/test1","super test");

// //It also create automatically the hierarchy when pushing new data for a DataPath that doesn't exists 
// db.push("/test2/my/test",5);

// //You can also push directly objects 
// db.push("/test3", {test:"test", json: {test:["test"]}});

// //If you don't want to override the data but to merge them 
// //The merge is recursive and work with Object and Array. 
// db.push("/test3", {new:"cool", json: {important : 5}}, false);
// /*
// This give you this results :
// {
//    "test":"test",
//    "json":{
//       "test":[
//          "test"
//       ],
//       "important":5
//    },
//    "new":"cool"
// }
// */
// //You can't merge primitive. 
// //If you do this: 
// db.push("/test2/my/test/",10,false);
// //the data will be overriden 

// //Get the data from the root 
// var data = db.getData("/");

// //From a particular DataPath 
// var data = db.getData("/test1");

// //If you try to get some data from a DataPath that doesn't exists 
// //You'll get an Error 
// try {
// var data = db.getData("/test1/test/dont/work");
// } catch(error) {
// //The error will tell you where the DataPath stopped. In this case test1 
// //Since /test1/test does't exist. 
//     console.error(error);
// }

// //Deleting data 
// db.delete("/test1");

// //Save the data (useful if you disable the saveOnPush) 
// db.save();

// //In case you have a exterior change to the databse file and want to reload it 
// //use this method 
// db.reload();

// Array Support
// You can also access the information stored into arrays and manipulate them.

// var JsonDB = require('node-json-db');
// //The second argument is used to tell the DB to save after each push 
// //If you put false, you'll have to call the save() method. 
// //The third argument is to ask JsonDB to save the database in an human readable format. (default false) 
// var db = new JsonDB("myDataBase", true, false);

// //This will create an array 'myarray' with the object '{obj:'test'}' at index 0 
// db.push("/arraytest/myarray[0]", {obj:'test'}, true);

// //You can retrieve a property of an object included in an array 
// //testString = 'test'; 
// var testString = db.getData("/arraytest/myarray[0]/obj");

// //Doing this will delete the object stored at the index 0 of the array. 
// //Keep in mind this won't delete the array even if it's empty. 
// db.delete(("/arraytest/myarray[0]");
// Appending in Array
// //You can also easily append new item to an existing array 
// //This set the next index with {obj: 'test'} 
// db.push("/arraytest/myarray[]", {obj:'test'}, true);


// //The append feature can be used in conjuction with properties 
// //This will set the next index as an object {myTest: 'test'} 
// db.push("/arraytest/myarray[]/myTest", 'test', true);
// Last Item in Array
// // Add basic array 
// db.push("/arraytest/lastItemArray", [1, 2, 3], true);

// //You can easily get the last item of the array with the index -1 
// //This will return 3 
// db.getData("/arraytest/lastItemArray[-1]");


// //You can delete the last item of an array with -1 
// //This will remove the integer "3" from the array 
// db.delete("/arraytest/lastItemArray[-1]");

// //This will return 2 since 3 just got removed 
// db.getData("/arraytest/lastItemArray[-1]");
// Exception/Error
// Type
// type	explanation
// DataError	When the error is linked to the Data Given
// DatabaseError	Linked to a problem with the loading or saving of the Database.
// Errors
// error	type	explanation
// The Data Path can't be empty	DataError	The Database expect to minimum receive the root / as DataPath.
// Can't find dataPath: /XXX. Stopped at YYY	DataError	When the full hierarchy of the DataPath given is not present in the Database. It tells you until where it's valid. This error can happen when using getData and delete
// Can't merge another type of data with an Array	DataError	If you chose to not override the data (merging) when pushing and the new data is an array but the current data isn't an array (an Object by example).
// Can't merge an Array with an Object	DataError	Same idea as the previous message. You have an array as current data and ask to merge it with an Object.
// DataPath: /XXX. YYY is not an array.	DataError	When trying to access an object as an array.
// DataPath: /XXX. Can't find index INDEX in array YYY	DataError	When trying to access a non-existent index in the array.
// Only numerical values accepted for array index	DataError	An array can only use number for its indexes. For this use the normal object.
// Can't Load Database: XXXX	DatabaseError	JsonDB can't load the database for "err" reason. You can find the nested error in error.inner
// Can't save the database: XXX	DatabaseError	JsonDB can't save the database for "err" reason. You can find the nested error in error.inner
// DataBase not loaded. Can't write	DatabaseError	Since the database hasn't been loaded correctly, the module won't let you save the data to avoid erasing your database.