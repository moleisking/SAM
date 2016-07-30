var express = require('express');
var passport = require('passport');
var jwt = require('jwt-simple');
var config = require('../config/settings');
var user = require("../core/user");
var emailer = require("../core/emailer");
var model = require("../models/user")

var apiRoutes = express.Router();

require('../config/passport')(passport);

apiRoutes.post('/signup', function (req, res) {
  if (!req.body.name || !req.body.pass)
    res.json({ success: false, message: 'Please pass name and password.' });
  else
    user.read(req.body.name, function (err, data) {
      if (err && err.id != 5)
        res.json({ success: false, message: 'Error in SignUp: ' + err });
      else
        if (data)
          res.json({ success: false, message: 'User Already Exists' });
        else
          user.create(req.body, function (err, dataCreated) {
            if (err)
              res.json({ success: false, message: JSON.stringify(err) });
            else
              res.json({ success: true, message: 'Successful created new user.' });
          });
    });
});

apiRoutes.post('/authenticate', function (req, res) {
  if (req.body.name === "" || req.body.name === "undefined" || req.body.pass === "" || req.body.pass === "undefined"
    || !req.body.name || !req.body.pass)
    res.json({ success: false, message: 'Must provide a user name and a password.' });
  else
    user.read(req.body.name, function (err, user) {
      if (err && err.id != 5)
        res.json({ success: false, message: JSON.stringify(err) });
      else
        if (!user)
          res.send({ success: false, message: 'Authentication failed. User not found.' });
        else {
          // check if password matches
          if (!model.validPassword(req.body.pass, user.pass))
            res.send({ success: false, message: 'Authentication failed. Wrong password.' });
          else {
            // if user is found and password is right create a token
            var token = jwt.encode(user, config.secret);
            // return the information including token as JSON
            res.json({ success: true, token: 'JWT ' + token });
          };
        }
    });
});

apiRoutes.get('/dashboard', passport.authenticate('jwt', { session: false }), function (req, res) {
  user.read(user.getNameFromTokenUser(req.headers), function (err, user) {
    if (err && err.id != 5)
      res.json({ success: false, message: JSON.stringify(err) });
    else
      if (!user)
        return res.status(403).send({ success: false, message: 'Authentication failed. User not found.' });
      else
        res.json({ success: true, message: 'Welcome in the dashboard ' + user.name + '!' });
  });
});

apiRoutes.post('/forgottenpassword', function (req, res) {
  if (!req.body.email)
    res.json({ success: false, message: 'Please provide email.' });
  else
    user.readByEmail(req.body.email, function (err, data) {
      if (err && err.id != 5)
        res.json({ success: false, message: err });
      else
        if (!data)
          res.json({ success: false, message: 'User dont exist.' });
        else
          emailer.forgottenpassword(data, function (err, data) {
            if (err)
              res.json({ success: false, message: err });
            else
              res.json({ success: true, message: data });
          });
    });
});

module.exports = apiRoutes;