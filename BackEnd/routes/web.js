var express = require("express");
var router = express.Router();
var web = require("../core/web");
var cat = require("../core/categories");
var emailer = require("../core/emailer");

router.get("/about", function (req, res, next) {
  web.about(function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ about: data });
  });
});

router.get("/termsconditions", function (req, res, next) {
  web.termsConditions(function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ termsconditions: data });
  });
});

router.get("/cookiepolicy", function (req, res, next) {
  web.cookiePolicy(function (err, data) {
    if (err)
      return res.status(500).json({ err });
    res.json({ cookiepolicy: data });
  });
});

router.post("/sendcontactform", function (req, res, next) {
  if (!req.body.name || !req.body.surname || !req.body.message || !req.body.email)
    return res.status(400).json({ app_err: "Please pass name, surname, message and email." });
  emailer.sendContactForm(req.body, function (err, status, body, headers) {
    if (err)
      return res.status(500).json({ err });
    res.json({ status, body, headers });
  });
});

router.get("/categories", function (req, res, next) {
  cat.all(function (err, data) {
    if (err)
      return res.status(500).json({ err });
    if (data.length === 0)
      return res.status(404).json({ "categories": data });
    res.json({ categories: data });
  });
});

module.exports = router;
