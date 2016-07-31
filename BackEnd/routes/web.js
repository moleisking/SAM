var express = require('express');
var router = express.Router();
var web = require("../core/web");

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

module.exports = router;