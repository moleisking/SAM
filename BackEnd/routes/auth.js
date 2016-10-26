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
  if (!req.body.username || !req.body.name || !req.body.surname || !req.body.email || !req.body.pass || !req.body.regLat ||
    !req.body.regLng || !req.body.category)
    return res.status(400).json({
      app_err: myLocals.translate("Please, provide username, name, surname, email, password, coordenades and category.")
    });
  user.read(req.body.email, function (err, data) {
    if (err && err.id != 5)
      return res.status(500).json({ err });
    if (data)
      return res.status(409).send({
        app_err:
        myLocals.translate("User already exists")
      });
    user.create(req.body, req.query.locale, function (err, data) {
      if (err)
        return res.status(500).json({ err });
      res.status(201).json({ signup: data });
    });
  });
});

router.post("/authenticate", function (req, res) {
  util.translate(myLocals, req.query.locale);
  if (!req.body.email || !req.body.pass)
    return res.status(400).json({ app_err: myLocals.translate("Please provide email and password.") });
  user.read(req.body.email, function (err, user) {
    if (err && err.id != 5)
      return res.status(500).json({ err });
    if (!user)
      return res.status(404).json({ app_err: myLocals.translate("Authentication failed.") });
    if (!model.validPassword(req.body.pass, user.pass))
      return res.status(403).json({ app_err: myLocals.translate("Authentication failed.") });
    delete user.image;
    var token = jwt.encode(user, config.secret);
    res.json({ token: "JWT " + token });
  });
});

router.get("/dashboard", passport.authenticate("jwt", { session: false }), function (req, res) {
  util.translate(myLocals, req.query.locale);
  user.read(user.getEmailFromTokenUser(req.headers), function (err, data) {
    if (err && err.id != 5)
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
    if (err && err.id != 5)
      return res.status(500).json({ err });
    if (!data)
      return res.status(404).json({ app_err: myLocals.translate("The user don't exist.") });
    emailer.forgottenpassword(data.email, data.pass, req.query.locale, function (err, status, body, headers) {
      if (err)
        return res.status(500).json({ err });
      res.json({ status, body, headers });
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
