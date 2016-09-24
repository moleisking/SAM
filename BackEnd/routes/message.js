var express = require("express");
var router = express.Router();
var passport = require("passport");
var message = require("../core/message");
var emailer = require("../core/emailer");
var user = require("../core/user");

router.get("/readalllasts", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  message.readAllLasts(user.getEmailFromTokenUser(req.headers), function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ data });
  });
});

router.get("/read/:nameUrl", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  message.readWith(user.getEmailFromTokenUser(req.headers), req.params.nameUrl, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ data });
  });
});

router.post("/add", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  if (!req.body.to || !req.body.text)
    return res.status(400).send("Please pass to and text.");
  message.create(user.getEmailFromTokenUser(req.headers), req.body, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ data });
  });
});

router.post("/email", function (req, res, next) {
  if (!req.body.from || !req.body.to || !req.body.text)
    return res.status(400).send("Please pass from, to and text.");
  emailer.email(req.body.from, req.body.to, req.body.text, function (err, status, body, headers) {
    if (err)
      return res.status(500).json({ err });
    res.json({ status, body, headers });
  });
});

module.exports = router;