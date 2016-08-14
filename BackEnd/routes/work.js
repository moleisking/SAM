var express = require('express');
var router = express.Router();
var work = require("../core/work");
var passport = require('passport');
var user = require("../core/user");

router.post('/create', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  if (!req.body.description || !req.body.name || !req.body.category)
    res.status(400).send("Please pass name, description and category.");
  else
    work.create(user.getNameFromTokenUser(req.headers), req.body, function (err, data) {
      if (err)
        res.status(500).json({err});
      else
        res.json({data});
    })
});

router.get('/allbyuser', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  work.allByUser(user.getNameFromTokenUser(req.headers), function (err, data) {
    if (err)
      res.status(500).json({err});
    else
      if (data == null)
        res.status(404).json({data});
      else
        res.json({data});
  })
});

module.exports = router;
