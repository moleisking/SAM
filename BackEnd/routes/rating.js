var express = require("express");
var router = express.Router();
var passport = require("passport");
var rating = require("../core/rating");
var user = require("../core/user");

// router.get("/readalllasts", passport.authenticate("jwt", { session: false }), function (req, res, next) {
//   message.readAllLasts(user.getEmailFromTokenUser(req.headers), function (err, data) {
//     if (err)
//       return res.status(500).json({ err });
//     res.json({ readalllasts: data });
//   });
// });

// router.get("/read/:nameUrl", passport.authenticate("jwt", { session: false }), function (req, res, next) {
//   if (!req.params.nameUrl)
//     return res.status(400).json({ app_err: "Please pass name url." });
//   message.readWith(user.getEmailFromTokenUser(req.headers), req.params.nameUrl, function (err, data) {
//     if (err)
//       return res.status(500).json({ err });
//     res.json({ read: data });
//   });
// });

router.post("/add", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  if (!req.body.id || !req.body.number)
    return res.status(400).json({ app_err: "Please pass also id, and number." });
  rating.create(user.getEmailFromTokenUser(req.headers), req.body, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    return res.json({ add: data });
  });
});

router.get("/readprofileauth/:nameUrl", passport.authenticate("jwt", { session: false }), function (req, res, next) {
  if (!req.params.nameUrl)
    return res.status(400).json({ app_err: "Please pass name url." });
  rating.readProfileAuth(user.getEmailFromTokenUser(req.headers), req.params.nameUrl, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    return res.json({ readprofile: data, myrating: data.myrating, average: data.average });
  });
});

router.get("/readprofile/:nameUrl", function (req, res, next) {
  if (!req.params.nameUrl)
    return res.status(400).json({ app_err: "Please pass name url." });
  rating.readProfile(req.params.nameUrl, function (err, data) {
    if (err)
      return res.status(500).json({ err });
    return res.json({ readprofile: data, average: data.average });
  });
});

module.exports = router;
