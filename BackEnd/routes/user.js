var express = require('express');
var router = express.Router();
var user = require("../core/user");
var passport = require('passport');

/* GET users listing. */
router.get('/', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  user.all(function (err, data) {
    if (err)
        res.status(500).json({err});
    else
        res.json({data});
  })
});

router.post('/saveprofile', passport.authenticate('jwt', { session: false }), function (req, res, next) {
  if (!req.body.description)
    res.status(400).send("Please pass description.");
  else
    user.saveProfile(user.getNameFromTokenUser(req.headers), req.body, function (err, data) {
      if (err)
        res.status(500).json({err});
      else
        res.json({data});
    })
});

router.get('/getmyprofile', passport.authenticate('jwt', { session: false }), function (req, res, next) {
    user.getProfile(user.getNameFromTokenUser(req.headers), function (err, data) {
      if (err)
        res.status(500).json({err});
      else
        res.json({data});
    })
});

router.get('/getprofile/:nameUrl', function (req, res, next) {
    user.getProfile(req.params.nameUrl, function (err, data) {
      if (err)
        res.status(500).json({err});
      else
        res.json({data});
    })
});

module.exports = router;
