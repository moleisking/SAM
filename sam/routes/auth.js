var express = require('express');
var router = express.Router();
var user = require("../core/user.core");
var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;
var userModel = require("../models/user.model")

passport.serializeUser(function (user, cb) {
  cb(null, user.name);
});

passport.deserializeUser(function (id, cb) {
  user.read(id, function (err, user) {
    cb(err, user);
  });
});

passport.use('login', new LocalStrategy({ passReqToCallback: true },
  function (req, username, password, cb) {
    // check if a user with username exists or not
    user.read(username,
      function (err, user) {
        // In case of any error, return using the cb method
        if (err)
          return cb(err);
        // Username does not exist, log error & redirect back
        if (!user)
          return cb(null, false, req.flash('loginMessage', 'User Not found with username ' + username));
        // User exists but wrong password, log the error 
        if (!userModel.validPassword(user, password))
          return cb(null, false, req.flash('loginMessage', 'Invalid Password'));
        // User and password both match, return user from 
        // cb method which will be treated like success
        return cb(null, user);
      }
    );
  }));

passport.use('signup', new LocalStrategy(
  {
    usernameField: 'name',
    passwordField: 'password',
    passReqToCallback: true
  },
  function (req, username, password, cb) {
    process.nextTick(function () {
      user.read(username, function (err, data) {
        // In case of any error return
        if (err) {
          if (err.id != 5)
            return cb(null, false, req.flash('signupMessage', 'Error in SignUp: ' + err));
        }
        // already exists
        if (data)
          return cb(null, false, req.flash('signupMessage', 'User Already Exists'));
        else {
          data = userModel.create();
          data.name = username;
          data.password = password;
          data.email = req.body.email;
          data.age = req.body.age;
          user.create(data, function (err, dataCreated) {
            if (err)
              return cb(null, false, req.flash('signupMessage', JSON.stringify(err)));
            else
              cb(null, dataCreated);
          });
        }
      });
    });
  })
);

/* GET login page. */
router.get('/login',
  function (req, res) {
    // Display the Login page with any flash message, if any
    res.render('login', { message: req.flash('loginMessage') });
  });

/* Handle Login POST */
router.post('/login',
  passport.authenticate('login', {
    successRedirect: '/auth',
    failureRedirect: '/login',
    failureFlash: true
  }));

/* GET Registration Page */
router.get('/signup',
  function (req, res) {
    res.render('user/create', { message: req.flash('signupMessage') });
  });

/* Handle Registration POST */
router.post('/signup',
  passport.authenticate('signup', {
    successRedirect: '/auth',
    failureRedirect: '/signup',
    failureFlash: true
  }));

router.get('/signout',
  function (req, res) {
    req.logout();
    res.redirect('/');
  });

module.exports = router;
