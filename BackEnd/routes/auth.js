var express = require("express");
var passport = require("passport");
var jwt = require("jwt-simple");
var config = require("../config/settings");
var user = require("../core/user");
var emailer = require("../core/emailer");
var model = require("../models/user");
var router = express.Router();
var util = require('../core/util');
var Localize = require("localize");
var myLocals = new Localize("localizations/user");

require("../config/passport")(passport);

router.post("/signup", function (req, res) {
  util.translate(myLocals, req.query.locale);
  //Check if fields are complete on form
  if (!req.body.username || !req.body.name || !req.body.email || !req.body.password || !req.body.regLat ||
    !req.body.regLng || !req.body.category)
    return res.status(400).json({
      app_err: myLocals.translate("Please, provide username, name, email, password, coordenades and category.")
    });
  //Check if user already exists
  user.read(req.body.email, function (err, data) {
    if (config.database_type == "nodedb" && err && err.id != 5) { 
    console.log(err);
    return res.status(500).json({ err });
    }
    //if (data)
    //  return res.status(409).send({ app_err: myLocals.translate("User already exists") });
     if (config.database_type == "nodedb" && String(data).indexOf("name") != -1)
    { 
      console.log("User already exists");     
      console.log(data);
      return res.status(409).send({ app_err: myLocals.translate("User already exists") });
    }
    else if (config.database_type == "mongodb" && String(data).indexOf("{}") != -1)
    { 
      console.log("User already exists");     
      console.log(data);
      return res.status(409).send({ app_err: myLocals.translate("User already exists") });
    }
    //Check if read finds user before starting create
    user.create(req.body, req.query.locale, function (err, data) {
      if (err)
        return res.status(500).json({ err });
      res.status(201).json({ signup: data });
    });
  });//end read
});

router.post("/authenticate", function (req, res) {
  util.translate(myLocals, req.query.locale);
  if (!req.body.email || !req.body.password)
    return res.status(400).json({ app_err: myLocals.translate("Please provide email and password.") });
  user.read(req.body.email, function (err, data) {
    if (config.database_type == "nodedb" && err && err.id != 5)
      return res.status(500).json({ err });
    if (!data)
      return res.status(404).json({ app_err: myLocals.translate("Authentication failed.") });
    if (!model.validPassword(req.body.password, data.password))
      return res.status(403).json({ app_err: myLocals.translate("Authentication failed.") });
    delete data.image;
    var token = jwt.encode(data, config.secret);
    res.json({ token: "JWT " + token });
  });
});

router.get("/dashboard", passport.authenticate("jwt", { session: false }), function (req, res) {
  util.translate(myLocals, req.query.locale);
  user.read(user.getEmailFromTokenUser(req.headers), function (err, data) {
    if (config.database_type == "nodedb" && err && err.id != 5)
      return res.status(500).json({ err });
    if (!data)
      return res.status(404).json({ app_err: myLocals.translate("Authentication failed.") });
    res.status(200).json({ dashboard: data });
  });
});

router.post("/forgottenpassword", function (req, res) {
  util.translate(myLocals, req.query.locale);
  if (!req.body.email)
    return res.status(400).json({ app_err: myLocals.translate("Please provide email.") });
  user.read(req.body.email, function (err, data) {
    if (config.database_type == "nodedb" && err && err.id != 5)
      return res.status(500).json({ err });
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
    return res.status(400).json({ app_err: myLocals.translate("Please provide passwords to validate.") });
  if (req.body.newpassword != req.body.confirmpassword)
    return res.status(400).json({ app_err: myLocals.translate("New password and confirm password must be the same.") });
  var email = user.getEmailFromTokenUser(req.headers);
  user.read(email, function (err, data) {
    if (config.database_type == "nodedb" && err && err.id != 5)
      return res.status(500).json({ err });
    if (!data)
      return res.status(500).json({ app_err: myLocals.translate("Authentication failed checking password.") });
    if (!model.validPassword(req.body.oldpassword, data.password))
      return res.status(500).json({ app_err: myLocals.translate("Authentication failed checking old password. Old password not valid.") });
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
      if (config.database_type == "nodedb" && err && err.id != 5)
        return res.status(500).json({ err });
      if (!data)
        return res.status(500).json({ app_err: myLocals.translate("Authentication failed checking password.") });
      user.changePassword(email, req.body.newpassword, req.query.locale, function (err, data) {
        res.json({ changeforgottenpassword: data });
      });
    });
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
