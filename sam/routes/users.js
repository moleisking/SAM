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

module.exports = router;
