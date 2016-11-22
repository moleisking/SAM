var express = require("express");
var router = express.Router();
var passport = require("passport");
var score = require("../dal/score");
var user = require("../dal/user");
var util = require("../util/util");
var Localize = require("localize");
var myLocals = new Localize("localizations/rating");
var uuid = require('node-uuid');

router.post("/add", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  util.translate(myLocals, req.query.locale);
  if (!req.body.id || !req.body.number)
    return res.status(400).json({ app_err: myLocals.translate("Please provide also id and number.") });
  score.create(user.getEmailFromTokenUser(req.headers), req.body, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    return res.json({ add: data });
  });
});

router.get("/readprofileauth/:url", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  util.translate(myLocals, req.query.locale);
  if (!req.params.url)
    return res.status(400).json({ app_err: myLocals.translate("Please provide url.") });
  score.readProfileAuth(user.getEmailFromTokenUser(req.headers), req.params.url, req.query.locale, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    return res.json({ readprofile: data, myrating: data.myrating, average: data.average });
  });
});

router.get("/readprofile/:url", function (req, res, next) {
  util.translate(myLocals, req.query.locale);
  if (!req.params.url)
    return res.status(400).json({ app_err: myLocals.translate("Please provide url.") });
  score.readProfile(req.params.url, req.query.locale, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    return res.json({ readprofile: data, average: data.average });
  });
});

module.exports = router;
