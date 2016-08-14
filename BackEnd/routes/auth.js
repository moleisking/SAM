var express = require('express');
var passport = require('passport');
var jwt = require('jwt-simple');
var config = require('../config/settings');
var user = require("../core/user");
var emailer = require("../core/emailer");
var model = require("../models/user");
var router = express.Router();

require('../config/passport')(passport);

router.post('/signup', function (req, res) {
  if (!req.body.name || !req.body.pass)
    res.status(400).send("Please provide name and password.");
  else
    user.read(req.body.name, function (err, data) {
      if (err && err.id != 5)
        res.status(500).json("Error in SignUp: " + err);
      else
        if (data)
          res.status(409).send("User Already Exists");
        else
          user.create(req.body, function (err, data) {
            if (err)
              res.status(500).json({ err });
            else
              res.status(201).json({ data });
          });
    });
});

router.post('/authenticate', function (req, res) {
  if (!req.body.name || !req.body.pass)
    res.status(400).send("Please provide name and password.");
  else
    user.read(req.body.name, function (err, user) {
      if (err && err.id != 5)
        res.status(500).json({ err });
      else
        if (!user)
          res.status(404).send("Authentication failed. User not found.");
        else {
          if (!model.validPassword(req.body.pass, user.pass))
            res.status(403).json("Authentication failed. Wrong password.");
          else {
            var token = jwt.encode(user, config.secret);
            res.json({ token: 'JWT ' + token });
          };
        }
    });
});

router.get('/dashboard', passport.authenticate('jwt', { session: false }), function (req, res) {
  user.read(user.getNameFromTokenUser(req.headers), function (err, data) {
    if (err && err.id != 5)
      res.status(500).json({ err });
    else
      if (!data)
        return res.status(404).send("Authentication failed. User not found.");
      else
        res.status(200).json({ data });
  });
});

router.post('/forgottenpassword', function (req, res) {
  if (!req.body.email)
    res.status(400).send("Please provide email.");
  else
    user.readByEmail(req.body.email, function (err, data) {
      if (err && err.id != 5)
        res.status(500).json({ err });
      else
        if (!data)
          res.status(404).send("User dont exist.");
        else
          emailer.forgottenpassword(data, function (err, data) {
            if (err)
              res.status(500).json({ err });
            else
              res.json({ data });
          });
    });
});

module.exports = router;