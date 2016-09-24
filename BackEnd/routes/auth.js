var express = require("express");
var passport = require("passport");
var jwt = require("jwt-simple");
var config = require("../config/settings");
var user = require("../core/user");
var emailer = require("../core/emailer");
var model = require("../models/user");
var router = express.Router();

require("../config/passport")(passport);

router.post("/signup", function (req, res) {
  if (!req.body.name || !req.body.email || !req.body.pass || !req.body.lat || !req.body.lng || !req.body.category)
    return res.status(400).send("Please provide name, email, password, coordenades and category.");
  user.read(req.body.email, function (err, data) {
    if (err && err.id != 5)
      return res.status(500).json("Error in SignUp: " + err);
    if (data)
      return res.status(409).send("User already exists");
    user.create(req.body, function (err, data) {
      if (err)
        return res.status(500).json({ err });
      res.status(201).json({ data });
    });
  });
});

router.post("/authenticate", function (req, res) {
  if (!req.body.email || !req.body.pass)
    return res.status(400).send("Please provide email and password.");
  user.read(req.body.email, function (err, user) {
    if (err && err.id != 5)
      return res.status(500).json({ err });
    if (!user)
      return res.status(404).send("Authentication failed. User not found.");
    if (!model.validPassword(req.body.pass, user.pass))
      return res.status(403).json("Authentication failed. Wrong password.");
    delete user.image;
    var token = jwt.encode(user, config.secret);
    res.json({ token: "JWT " + token });
  });
});

router.get("/dashboard", passport.authenticate("jwt", { session: false }), function (req, res) {
  user.read(user.getEmailFromTokenUser(req.headers), function (err, data) {
    if (err && err.id != 5)
      return res.status(500).json({ err });
    if (!data)
      return res.status(404).send("Authentication failed. User not found.");
    res.status(200).json({ data });
  });
});

router.post("/forgottenpassword", function (req, res) {
  if (!req.body.email)
    return res.status(400).send("Please provide email.");
  user.read(req.body.email, function (err, data) {
    if (err && err.id != 5)
      return res.status(500).json({ err });
    if (!data)
      return res.status(404).send("User dont exist.");
    emailer.forgottenpassword(data.email, data.pass, function (err, status, body, headers) {
      if (err)
        return res.status(500).json({ err });
      res.json({ status, body, headers });
    });
  });
});

module.exports = router;