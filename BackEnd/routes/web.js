var express = require('express');
var router = express.Router();
var web = require("../core/web");
var cat = require("../core/categories");
var emailer = require("../core/emailer");

router.get('/about', function (req, res, next) {
  web.about(function (err, data) {
    if (err)
      res.status(500).json({err});
    else
      res.json({data});
  })
});

router.get('/termsconditions', function (req, res, next) {
  web.termsConditions(function (err, data) {
    if (err)
      res.status(500).json({err});
    else
      res.json({data});
  })
});

router.post('/sendcontactform', function (req, res, next) {
  if (!req.body.message || !req.body.email)
    res.status(400).send("Please pass message and email.");
  else
    emailer.sendContactForm(req.body, function (err, data) {
      if (err)
        res.status(500).json({err});
      else
        res.json({data});
    });
});

router.get('/categories', function (req, res, next) {
  cat.all(function (err, data) {
    if (err)
      res.status(500).json({err});
    else if (data.length === 0)
      res.status(404).json({data});
    else
      res.json({data});
  })
});

module.exports = router;