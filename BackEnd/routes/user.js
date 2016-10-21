var express = require("express");
var router = express.Router();
var user = require("../core/user");
var passport = require("passport");

router.get("/", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  user.all(function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ all: data });
  });
});

router.post("/saveprofile", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  if (!req.body.description || !req.body.mobile || !req.body.address || !req.body.category)
    return res.status(400).json({ app_err: "Please pass description, category, mobile and address." });
  req.body.image = req.body.image.replace(/ /g, "+");
  user.saveProfile(user.getEmailFromTokenUser(req.headers), req.body, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ profile_saved: data });
  });
});

router.get("/getmyprofile", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  user.getMyProfile(user.getEmailFromTokenUser(req.headers), function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ myuser: data });
  });
});

router.get("/getprofile/:nameUrl", function (req, res, next) {
  if (!req.params.nameUrl)
    return res.status(400).json({ app_err: "Please pass name url." });
  user.getProfile(req.params.nameUrl, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ profile: data });
  });
});

router.post("/search", function (req, res, next) {
  if (!req.body.regLat || !req.body.regLng || !req.body.category || !req.body.radius)
    return res.status(400).json({ app_err: "Please pass radius, category, lat and lng." });
  user.search(req.body, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ users: data });
  });
});

module.exports = router;
