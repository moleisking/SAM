var express = require("express");
var router = express.Router();
var passport = require("passport");
var message = require("../core/message");
var user = require("../core/user");

router.get("/read", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  message.read(user.getEmailFromTokenUser(req.headers), function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ data });
  });
});

router.post("/add", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  if (!req.body.to || !req.body.text)
    return res.status(400).send("Please pass from, to and text.");
  message.create(user.getEmailFromTokenUser(req.headers), req.body, function (err) {
    if (err)
      return res.status(500).json({ err });
    res.json({ data });
  });
});

module.exports = router;
