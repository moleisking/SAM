var express = require('express');
var passport = require('passport');
var jwt = require('jwt-simple');
var user = require("../core/user");
var config = require('../config/settings');
var model = require("../models/user")

var apiRoutes = express.Router();

// pass passport for configuration
require('../config/passport')(passport);

// create a new user account (POST http://localhost:8080/api/signup)
apiRoutes.post('/signup', function (req, res) {
  if (!req.body.name || !req.body.pass)
    res.json({ success: false, message: 'Please pass name and password.' });
  else
    user.read(req.body.name, function (err, data) {
      // In case of any error return
      if (err && err.id != 5)
        res.json({ success: false, message: 'Error in SignUp: ' + err });
      else
        if (data)
          res.json({ success: false, message: 'User Already Exists' });
        else {
          data = model.create();
          data.name = req.body.name;
          data.pass = req.body.pass;
          // data.email = req.body.email;
          user.create(data, function (err, dataCreated) {
            if (err)
              res.json({ success: false, message: JSON.stringify(err) });
            else
              res.json({ success: true, message: 'Successful created new user.' });
          });
        }
    });
});

apiRoutes.post('/login', function (req, res) {
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
  var token = getToken(req.headers);
  if (token) {
    var decoded = jwt.decode(token, config.secret);
    user.read(decoded.name, function (err, user) {
      if (err && err.id != 5)
        res.json({ success: false, message: JSON.stringify(err) });
      else
        if (!user)
          return res.status(403).send({ success: false, message: 'Authentication failed. User not found.' });
        else
          res.json({ success: true, message: 'Welcome in the dashboard ' + user.name + '!' });
    });
  } else
    return res.status(403).send({ success: false, message: 'No token provided.' });
});

getToken = function (headers) {
  if (headers && headers.authorization) {
    var parted = headers.authorization.split(' ');
    if (parted.length === 2)
      return parted[1];
    else
      return null;
  } else
    return null;
};

module.exports = apiRoutes;