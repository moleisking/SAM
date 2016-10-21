var express = require("express");
var router = express.Router();
var passport = require("passport");
var user = require("../core/user");

router.post("/addcredit", passport.authenticate("jwt", { session: false }), function (req, res, next) {
    if (!req.body.value)
        return res.status(400).json({ app_err: "Please pass value." });
    user.addCredit(user.getEmailFromTokenUser(req.headers), req.body.value, function (err, data) {
        if (err)
            return res.status(500).json({ err });
        res.json({ transaction: data.credit });
    });
});

module.exports = router;
