var express = require('express');
var router = express.Router();
var work = require("../core/work");
var passport = require('passport');

router.post('/create', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  if (!req.body.description || !req.body.name || !req.body.category)
    res.json({ success: false, message: 'Please pass name, description and category.' });
  else
    work.create(req.body, function (err, data) {
      if (err)
        res.json({ success: false, message: err });
      else
        res.json({ success: true, data: data });
    })
});

module.exports = router;
