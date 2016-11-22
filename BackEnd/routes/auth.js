var express = require("express");
var passport = require("passport");
var jwt = require("jwt-simple");
var config = require("../config/settings");
var user = require("../dal/user");
var emailer = require("../dal/emailer");
var model = require("../models/user");
var router = express.Router();
var util = require('../util/util');
var Localize = require("localize");
var myLocals = new Localize("localizations/user");
var uuid = require('node-uuid');

//used to actually point to custom config/passport.js
require("../util/passport")(passport);

router.post("/signup", function (req, res) {
  console.log("route signup called");   
 
  if (req.body.username === "" || req.body.name === "" || req.body.email === "" || req.body.password === "" || req.body.regLat === "" ||
    req.body.regLng === "") {    
    //error not all fields filled in
     return res.status(400).json({ backend_route_err: "Please, provide username, name, email, password and coordinates." });
  }
  else
  { 
    //generate user id to prepare for create
    req.body.id = uuid.v1();
    req.body.guid = uuid.v1();  
  } 
  
  //check for duplicate user
  user.read(req.body.email, function (err, data) {
   
    if (err ){ return res.status(500).json({ err });}
      
    if (JSON.stringify(data).indexOf("name") >= 0) {
        //data returned "name" so user found
        console.log("route user signup data returned with name");
        console.log(data)
        res.json(data);
        return res.status(409).send({ app_err: myLocals.translate("User already exists") });
    }
    else if (JSON.stringify(data) === "[]") {
        //empty array returned so new user must be created
        console.log("route user signup data returned without name");
         
        res.json(data);
        user.create(req.body,  function (err, data) {
            if (err) {
                console.log(err);
                return res.status(500).json({ err });
            }       
            res.status(201).json({ signup: data });
        });       
        
    } 
    
  });
});

router.post("/authenticate", function (req, res) {
    console.log("route authenticate called");   
    //util.translate(myLocals, req.query.locale);
    if (req.body.email === "" || req.body.password === "") {
         return res.status(400).json({ backend_route_err: "Please provide email and password." });
    }
       
    user.read(req.body.email, function (err, data) {
        console.log("route authenticate user read");   
        if (err)
        {
            console.log("route authenticate reply error");
            return res.status(500).json({ err });
        }    
           
        if (JSON.stringify(data).indexOf("name") >= 0) {
            //data returned "name" so user found
            console.log("route user signup data returned with name");
           
             if (!model.validPassword(req.body.password, data[0].password.toString())) {
                 console.log("Passwords do not match");
                // res.json({ auth_err: "passwords do not match" })
                 return res.status(403).json({ app_err: "Authentication failed." });
             }
             else if (model.validPassword(req.body.password, data[0].password.toString()))
             { 
                console.log("Password match");               
                var token = jwt.encode(data, config.secret);
                res.json({ token: "JWT " + token });
             }
        }
        else if (JSON.stringify(data) === "[]")
        { 
            //empty array returned so no user found           
            return res.status(204).json({ app_err: "User not Found" });
        }   
       
    });
});

router.get("/dashboard", passport.authenticate("jwt", { session: false }), function (req, res) {  
    
// var token = jwt.encode(data, config.secret);

 console.log("dashboard route success");
 console.log(util.getEmailFromTokenUser(req.headers));
    
    user.read(util.getEmailFromTokenUser(req.headers), function (err, data) {
        if (err)
        {
            console.log("dashboard if error");
            return res.status(500).json({ err });
        }

        if (JSON.stringify(data).indexOf("name") >= 0) {
            //data returned "name" so user found
            console.log("route user dashboard data returned with name");
            console.log(data)
            res.json(data);
            return res.status(200).send({ app_err: "User already exists" });
        }
        else if (JSON.stringify(data) === "[]") {
            //empty array returned so new user must be created
            console.log("route user dashboard data returned without name");
         
            res.json(data);
           return res.status(404).json({ app_err: myLocals.translate("Authentication failed.") });        
        }       
        
    });
});

router.post("/forgottenpassword", function (req, res) {
    util.translate(myLocals, req.query.locale);
    if (!req.body.email)
        return res.status(400).json({ app_err: myLocals.translate("Please provide email.") });
    user.read(req.body.email, function (err, data) {
        if (err)
        { 
            return res.status(500).json({ err });
        }
            
        if (!data)
            return res.status(404).json({ app_err: myLocals.translate("The user don't exist.") });
        emailer.forgottenpassword(data.email, data.guid, req.query.locale, function (err, status, body, headers) {
            if (err)
                return res.status(500).json({ err });
            res.json({ status, body, headers });
        });
    });
});

router.post("/changepassword", passport.authenticate("jwt", { session: false }), function (req, res) {
    util.translate(myLocals, req.query.locale);
    if (!req.body.oldpassword || !req.body.newpassword || !req.body.confirmpassword)
    { 
         return res.status(400).json({ app_err: myLocals.translate("Please provide passwords to validate.") });
    }
       
    if (req.body.newpassword != req.body.confirmpassword)
        return res.status(400).json({ app_err: myLocals.translate("New password and confirm password must be the same.") });
    var email = user.getEmailFromTokenUser(req.headers);
    user.read(email, function (err, data) {
        if (err)
        { 
            return res.status(500).json({ err });
        }
           
        if (!data)
            return res.status(500).json({ app_err: myLocals.translate("Authentication failed checking password.") });
        if (!model.validPassword(req.body.oldpassword, data.password))
        { 
            return res.status(500).json({ app_err: myLocals.translate("Authentication failed checking old password. Old password not valid.") });
        }
            
        user.changePassword(email, req.body.newpassword, req.query.locale, function (err, data) {
            res.json({ changepassword: data });
        });
    });
});

router.post("/changeforgottenpassword", function (req, res) {
    util.translate(myLocals, req.query.locale);
    if (!req.body.oldpassword || req.body.oldpassword.length !== 32 || !req.body.newpassword || !req.body.confirmpassword)
        return res.status(400).json({ app_err: myLocals.translate("Please provide passwords to validate.") });
    if (req.body.newpassword != req.body.confirmpassword)
        return res.status(400).json({ app_err: myLocals.translate("New password and confirm password must be the same.") });
    user.getEmailByGuid(req.body.oldpassword, function (err, email) {
        if (err)
            return res.status(500).json({ err });
        user.read(email, function (err, data) {
            if (err)
                return res.status(500).json({ err });
            if (!data)
                return res.status(500).json({ app_err: myLocals.translate("Authentication failed checking password.") });
            user.changePassword(email, req.body.newpassword, req.query.locale, function (err, data) {
                res.json({ changeforgottenpassword: data });
            });
        });
    });
});

router.post("/activate", function (req, res) {
   
    if (!req.body.code || req.body.code.length != 32)
    {
        return res.status(400).json({ app_err: "Please provide code to validate." });
    }

    user.updateGuid(req.body.guid,  function (err, data) {           
        if (err)
        {
            return res.status(500).json({ err });
        }              
        //nMatched 1 nUpserted 0 nModified 1
        //acknowledged true matchedCount 1 modfiedCount 1
            if (JSON.stringify(data).indexOf("acknowledged") >= 0)
            { 
                res.status(200).json(data);
            }         
            else if (JSON.stringify(data) === "[]")
            {
                 res.status(204).json(data);
                 res.json(data);
            }               
            res.json({ activate: data.activate });
        });   
});

module.exports = router;

// var Localize = require('localize');
// var myLocalize = new Localize({
//     "Testing...": {
//         "es": "Pruebas...",
//         "sr": "тестирање..."
//     },
//     "Substitution: $[1]": {
//         "es": "Sustitución: $[1]",
//         "sr": "замена: $[1]"
//     }
// });
// console.log(myLocalize.translate("Testing...")); // Testing...
// console.log(myLocalize.translate("Substitution: $[1]", 5)); // Substitution: 5
// myLocalize.setLocale("es");
// console.log(myLocalize.translate("Testing...")); // Pruebas...
// myLocalize.setLocale("sr");
// console.log(myLocalize.translate("Substitution: $[1]", 5)); // замена: 5

//1xx Informational.
//2xx Success.
//3xx Redirection.
//4xx Client Error.
//5xx Server Error.

//app.post('/signup', passport.authenticate('local', {
// successRedirect: '/signin',
// failureRedirect: '/signup',
// failureFlash: true
//}));
