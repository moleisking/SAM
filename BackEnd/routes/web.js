var express = require('express');
var router = express.Router();
var web = require("../core/web");
var cat = require("../core/categories");
var emailer = require("../core/emailer");

router.get('/about', function (req, res, next) {
  web.about(function (err, data) {
    if (err)
      res.json({ success: false, message: err });
    else
      res.json({ success: true, data: data });
  })
});

router.get('/termsconditions', function (req, res, next) {
  web.termsConditions(function (err, data) {
    if (err)
      res.json({ success: false, message: err });
    else
      res.json({ success: true, data: data });
  })
});

router.post('/sendcontactform', function (req, res, next) {
  if (!req.body.message || !req.body.email)
    res.json({ success: false, message: 'Please pass message and email.' });
  else
    emailer.sendContactForm(req.body, function (err, data) {
      if (err) {
        // res.status(500);
        return next(err);
      }
      else
        res.json({ success: true, message: data });
    });
});

router.get('/categories', function (req, res, next) {
  cat.all(function (err, data) {
    if (err)
      res.json({ success: false, message: err });
    else
      res.json({ success: true, data: data });
  })
});

module.exports = router;