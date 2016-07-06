var express = require('express');
var router = express.Router();

//test middleware
router.use(function (req, res, next) {
  console.log('Time:', Date.now());
  next();
});

//code to set the language for every request after setting a cookie when choose a language
router.use(function (req, res, next) {
    try {
        var cookieLanguage = req.cookies.locales;
        res.setLocale(cookieLanguage);
    }
    catch (err) {
        res.setLocale('en');
    }
    next();
});

module.exports = router;
