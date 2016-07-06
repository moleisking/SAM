var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'SAM', greeting: res.__('Hello') });
});

router.get('/es', function (req, res, next) {
  res.cookie('locales', "es");
  res.redirect('/');
});

router.get('/en', function (req, res, next) {
  res.cookie('locales', "en");
  res.redirect('/');
});

router.get('/auth', function (req, res, next) {
  if (req.isAuthenticated())
    return next();
  res.redirect('/login');
}, function (req, res) {
  res.render('auth', { user: req.user });
});

// As with any middleware it is quintessential to call next()
// if the user is authenticated
// var isAuthenticated = function (req, res, next) {
//   if (req.isAuthenticated())
//     return next();
//   res.redirect('/');
// };

module.exports = router;
