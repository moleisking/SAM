var express = require('express');
var router = express.Router();
var user = require("../core/user");
var passport = require('passport');

/* GET users listing. */
router.get('/', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  user.all(function (err, data) {
    if (err)
      res.json({ success: false, message: err });
    else
      res.json({ success: true, data: data });
  })
});

router.post('/saveprofile', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  if (!req.body.description || !req.body.name)
    res.json({ success: false, message: 'Please pass description and name.' });
  else
    user.saveProfile(req.body, function (err, data) {
      if (err)
        res.json({ success: false, message: err });
      else
        res.json({ success: true, data: data });
    })
});

router.post('/getprofile', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  if (!req.body.name)
    res.json({ success: false, message: 'Please pass name.' });
  else
    user.getProfile(req.body.name, function (err, data) {
      if (err)
        res.json({ success: false, message: err });
      else
        res.json({ success: true, data: data });
    })
});

module.exports = router;
