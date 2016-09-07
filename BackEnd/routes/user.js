var express = require('express');
var router = express.Router();
var user = require("../core/user");
var passport = require('passport');

router.get('/', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  user.all(function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ data });
  })
});

router.post('/saveprofile', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  if (!req.body.description && !req.body.mobile && !req.body.address)
    return res.status(400).send("Please pass description, mobile and address.");
  user.saveProfile(user.getEmailFromTokenUser(req.headers), req.body, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ data });
  })
});

router.get('/getmyprofile', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  user.getMyProfile(user.getEmailFromTokenUser(req.headers), function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ data });
  })
});

router.get('/getprofile/:nameUrl', function (req, res, next) {
  user.getProfile(req.params.nameUrl, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ data });
  })
});

module.exports = router;
